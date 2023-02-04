package com.example.demo.service;

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

    private final ArrayBlockingQueue<String> myBlockingQueue = new ArrayBlockingQueue<>(1000, true);
    private final AtomicBoolean connectFlag =  new AtomicBoolean(false);

    private ConnectableObservable<String> observable = Observable.create(emitter ->
            Observable.interval(5, TimeUnit.SECONDS).subscribe(i ->{
                try {
                    if (myBlockingQueue.isEmpty()) {
                        connectFlag.getAndSet(false);
                        emitter.onComplete();
                    } else {
                        emitter.onNext(myBlockingQueue.poll());
                    }
                }catch (Exception e){
                    emitter.onError(e);
                }
            })
    ).map(event -> (String) event).publish();

    //public ConnectableObservable<String> getCoffeeStream(){
    //    return observable;
    //}
}
