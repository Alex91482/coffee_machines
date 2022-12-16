package com.example.demo.service;

import com.example.demo.dao.SavedEventDAOImpl;
import com.example.demo.entity.SavedEvent;
import com.example.demo.service.beverages.AbstractCoffeeBeverages;
import com.example.demo.service.beverages.EnumBeverages;
import com.example.demo.util.idgenerator.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CoffeeMachineService {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineService.class);
    private final SavedEventDAOImpl savedEventDAOImpl; //взаимодействие с бд
    private final BeveragesCoffeeFactory beveragesCoffeeFactory; //фабрика по созданию напитков
    private final IdGenerator idGenerator; //генератор id для сущностей

    public CoffeeMachineService(SavedEventDAOImpl savedEventDAOImpl, BeveragesCoffeeFactory beveragesCoffeeFactory
                ,IdGenerator idGenerator){
        this.savedEventDAOImpl = savedEventDAOImpl;
        this.beveragesCoffeeFactory = beveragesCoffeeFactory;
        this.idGenerator = idGenerator;
    }

    //размер очереди ограничен 100 записями, далее они будут перезаписыватся
    private final ArrayBlockingQueue<SavedEvent> myBlockingQueue = new ArrayBlockingQueue<>(100, true);
    private final AtomicBoolean connectFlag = new AtomicBoolean(false);

    private final ConnectableFlux<SavedEvent> myEventGenerator = Flux.create(event ->{
        try{
            while(true){
                Thread.sleep(5000);
                if(myBlockingQueue.isEmpty()){
                    break;
                }
                event.next(myBlockingQueue.poll());
            }
            connectFlag.getAndSet(false);
            event.complete();
        }catch (Exception e) {
            logger.error("Exception in loop publisher: {}", e.getMessage());
            event.error(new RuntimeException(e));
        }
    }).map(event -> (SavedEvent) event).publish();


    public Flux<String> getBeverages(EnumBeverages enumBeverages) {
        return getMonoLastEvent()
                .flatMapMany(result -> {

                    SavedEvent currentEvents = subtractTheIngredients(result, enumBeverages);

                    if(!currentEvents.getOccurredEvent().equals("Not enough ingredients. Fill the tank with coffee and water.")) {
                        myBlockingQueue.add(currentEvents); //добавляем событие в очередь
                        delayedStart();                       //запускаем на всех один поток публикации
                        return myEventGenerator.takeWhile(event -> { //выполняем проверку что если заказанный напиток изготовлен прикращаем потреблять события
                            logger.info("event {} time {}, currentEvent {} time {}", event.getId(),event.getEventTime(), currentEvents.getId(), currentEvents.getEventTime());
                            return event.getEventTime().isBefore(currentEvents.getEventTime()) || event.getEventTime().isEqual(currentEvents.getEventTime());
                        }).map(SavedEvent::getOccurredEvent);

                    }else{
                        return Flux.just(currentEvents.getOccurredEvent());
                    }
                });
    }

    private void delayedStart(){
        if (!connectFlag.getAndSet(true)) {
            Runnable task = () -> {
                try {
                    Thread.sleep(500);
                    myEventGenerator.connect();
                } catch (Exception e) {
                    logger.error("Exception delayed start {}", e.getMessage());
                    System.err.println(e);
                }
            };
            Thread th1 = new Thread(task);
            th1.start();
        }
    }

    public Flux<String> setCoffeeAndWater(CoffeeAndWaterEnum coffeeAndWaterEnum){
        //метод заполняет баки с кофе и водой
        //метод будет принемать енум для заполнение кофе или воды или одновременно кофе и воды
        return getMonoLastEvent()
                .flatMapMany(result -> {
                    int waterLevel = 1000;
                    int coffeeLevel = 1000;

                    switch (coffeeAndWaterEnum){
                        case COFFEE -> {
                            waterLevel = result.getFillTheWaterTank();
                        }
                        case WATER -> {
                            coffeeLevel = result.getFillCoffeeTank();
                        }
                    }

                    SavedEvent savedEvent = new SavedEvent().builder()
                            .id(idGenerator.getRandomId())
                            .occurredEvent(coffeeAndWaterEnum.getEvent())
                            .eventTime(LocalDateTime.now())
                            .fillTheWaterTank(waterLevel)
                            .fillCoffeeTank(coffeeLevel)
                            .build();

                    savedEventDAOImpl.save(savedEvent);
                    myBlockingQueue.add(savedEvent);
                    delayedStart();
                    return myEventGenerator.takeWhile(event -> { //выполняем проверку что если заказанный напиток изготовлен прикращаем потреблять события
                        logger.info("event {} time {}, currentEvent {} time {}", event.getId(),event.getEventTime(), savedEvent.getId(), savedEvent.getEventTime());
                        return event.getEventTime().isBefore(savedEvent.getEventTime()) || event.getEventTime().isEqual(savedEvent.getEventTime());
                    }).map(SavedEvent::getOccurredEvent);
                });
    }

    private SavedEvent subtractTheIngredients(SavedEvent lastSavedEvent, EnumBeverages typeBeverages){
        //проводим вычитания из текущих показаний хватит ли на напиток кофе и воды
        AbstractCoffeeBeverages beverages = beveragesCoffeeFactory.createCoffeeBeverages(typeBeverages);
        int waterLevel = lastSavedEvent.getFillTheWaterTank() - beverages.getWaterConsumption();
        int coffeeLevel = lastSavedEvent.getFillCoffeeTank() - beverages.getCoffeeConsumption();

        SavedEvent savedEvent;
        if(waterLevel >= 0 && coffeeLevel >= 0){
            savedEvent = new SavedEvent().builder()
                    .id(idGenerator.getRandomId())
                    .occurredEvent(typeBeverages.name())
                    .eventTime(LocalDateTime.now())
                    .fillTheWaterTank(waterLevel)
                    .fillCoffeeTank(coffeeLevel)
                    .build();
            savedEventDAOImpl.save(savedEvent);
        }else{
            savedEvent = new SavedEvent().builder()
                    .id(idGenerator.getRandomId())
                    .occurredEvent("Not enough ingredients. Fill the tank with coffee and water.")
                    .eventTime(LocalDateTime.now())
                    .fillTheWaterTank(lastSavedEvent.getFillTheWaterTank())
                    .fillCoffeeTank(lastSavedEvent.getFillCoffeeTank())
                    .build();
        }

        return savedEvent;
    }

    public Mono<SavedEvent> getMonoLastEvent(){
        return savedEventDAOImpl.getTheLatestEntry();
    }

    public int getSizeQueue(){
        return myBlockingQueue.size();
    }

    public Mono<List<SavedEvent>> getByAllEventToStartMachine(){
        //получить все события "Coffee Machine start" используется псрото для примера
        return savedEventDAOImpl.findByOccurredName("Coffee Machine start").collectList();
    }

    public Mono<List<SavedEvent>> getByAllEventToAmericano(){
        return savedEventDAOImpl.findByOccurredName("Americano").collectList();
    }

    public Mono<List<SavedEvent>> getByAllEventToEspresso(){
        return savedEventDAOImpl.findByOccurredName("Espresso").collectList();
    }

    public Mono<List<SavedEvent>> getByAllEventToDoubleEspresso(){
        return savedEventDAOImpl.findByOccurredName("DoubleEspresso").collectList();
    }
}
