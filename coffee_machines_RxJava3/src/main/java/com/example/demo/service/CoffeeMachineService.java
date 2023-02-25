package com.example.demo.service;

import com.example.demo.dao.SavedEventDAO;
import com.example.demo.entity.CoffeeEvent;
import com.example.demo.entity.SavedEvent;
import com.example.demo.entity.beverages.TypesCoffeeEvent;
import com.example.demo.util.IdGenerator;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CoffeeMachineService {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineService.class);

    private final SavedEventDAO savedEventDAO;
    private final IdGenerator idGenerator;

    public  CoffeeMachineService(SavedEventDAO savedEventDAO, IdGenerator idGenerator){
        this.savedEventDAO = savedEventDAO;
        this.idGenerator = idGenerator;
    }

    private final ArrayBlockingQueue<SavedEvent> myBlockingQueue = new ArrayBlockingQueue<>(1000, true);
    private final AtomicBoolean connectFlag =  new AtomicBoolean(false);

    private final ConnectableObservable<SavedEvent> observable = Observable.create(emitter ->
            Observable.interval(2, TimeUnit.SECONDS).subscribe(i ->{
                try {
                    if (myBlockingQueue.isEmpty()) {
                        connectFlag.getAndSet(false);
                        emitter.onComplete();
                    } else {
                        emitter.onNext(myBlockingQueue.poll());
                    }
                }catch (Exception e){
                    logger.error("Exception while fetching element: {}", e.getMessage());
                    emitter.onError(e);
                }
            })
    ).map(event -> (SavedEvent) event).publish();

    public Observable<String> getCoffeeStreamTest(TypesCoffeeEvent typesCoffeeEvent){
        return coffeeProducer(typesCoffeeEvent)
                .map(savedEvent -> savedEvent.getCoffeeEvent()
                        .getTypesCoffeeEvent()
                        .name()
                );
    }

    private Observable<SavedEvent> coffeeProducer(TypesCoffeeEvent typesCoffeeEvent){
        return savedEventDAO.findFirstByOrderByEventTimeDesc()
                .toObservable()
                .flatMap(result -> {
            var waterLevel = result.getFillTheWaterTank() - typesCoffeeEvent.getWaterLevel();
            var coffeeLevel = result.getFillCoffeeTank() - typesCoffeeEvent.getCoffeeLevel();
            switch (typesCoffeeEvent){
                case Americano:
                case Espresso:
                case DoubleEspresso:
                    if(waterLevel < 0 || coffeeLevel < 0){
                        var savedEvent =
                                createSaveEvent(TypesCoffeeEvent.Not_Enough_Ingredients,waterLevel, coffeeLevel);
                        savedEventDAO.save(savedEvent);
                        return Observable.just(savedEvent);
                    }
                    var savedEvent = createSaveEvent(typesCoffeeEvent, waterLevel, coffeeLevel);
                    myBlockingQueue.add(savedEvent);
                    startObserver();
                    return observable.takeWhile(currentEvents -> {
                        logger.info("event {} time {}, currentEvent {} time {}",
                                savedEvent.getId(),savedEvent.getEventTime(),
                                currentEvents.getId(), currentEvents.getEventTime()
                        );
                        return savedEvent.getEventTime().isBefore(currentEvents.getEventTime())
                                || savedEvent.getEventTime().isEqual(currentEvents.getEventTime());
                    });
                case Filling_Coffee:
                case Filling_Water:
                case Filling_Coffee_And_Water:
                    return Observable.just(createSavedEventTank(typesCoffeeEvent, waterLevel, coffeeLevel));
                default:
                    return Observable.just(createSaveEvent(typesCoffeeEvent, waterLevel, coffeeLevel));
            }
        });
    }

    private void startObserver(){
        if(!connectFlag.getAndSet(true)){
            observable.connect();
        }
    }

    private SavedEvent createSaveEvent(TypesCoffeeEvent typesCoffeeEvent, int waterLevel, int coffeeLevel){
        return new SavedEvent(
                idGenerator.generation(),
                LocalDateTime.now(),
                waterLevel,
                coffeeLevel,
                new CoffeeEvent(typesCoffeeEvent));
    }

    private SavedEvent createSavedEventTank(TypesCoffeeEvent typesCoffeeEvent, int waterLevel, int coffeeLevel){
        return switch (typesCoffeeEvent){
            case Filling_Coffee -> createSaveEvent(typesCoffeeEvent, waterLevel, 1000);
            case Filling_Water -> createSaveEvent(typesCoffeeEvent, 1000, coffeeLevel);
            case Filling_Coffee_And_Water -> createSaveEvent(typesCoffeeEvent, 1000, 1000);
            default -> createSaveEvent(typesCoffeeEvent, waterLevel, coffeeLevel);
        };
    }
}
