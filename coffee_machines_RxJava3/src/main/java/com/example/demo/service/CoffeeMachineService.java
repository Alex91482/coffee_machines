package com.example.demo.service;

import com.example.demo.entity.SavedEvent;
import com.example.demo.entity.beverages.TypesCoffeeEvent;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CoffeeMachineService {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineService.class);

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
        //myBlockingQueue.add(savedEvent1);
        observable.connect();
        return observable.map(savedEvent -> savedEvent.getCoffeeEvent().getTypesCoffeeEvent().name());
    }
}
