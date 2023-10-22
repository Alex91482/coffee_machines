package com.example.demo;

import com.example.demo.entity.SavedEvent;
import com.example.demo.service.BeveragesCoffeeFactory;
import com.example.demo.service.beverages.EnumBeverages;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestService {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ArrayBlockingQueue<SavedEvent> myBlockingQueue = new ArrayBlockingQueue<>(100, true);

    private final ConnectableFlux<SavedEvent> myEventGenerator = Flux.create(event -> {
        try{
            while(!myBlockingQueue.isEmpty()){
                SavedEvent se = myBlockingQueue.poll();
                Thread.sleep(2000);
                event.next(se);
            }
            event.complete();
        }catch (Exception e) {
            event.error(new RuntimeException(e));
        }
    })
            .map(event -> (SavedEvent) event)
            .publish()
            ;

    public Flux<String> getBeverages(EnumBeverages enumBeverages){
        myBlockingQueue.add(
                new SavedEvent().builder()
                        .id(1000L)
                        .occurredEvent(enumBeverages.name())
                        .eventTime(LocalDateTime.now())
                        .fillTheWaterTank(1000)
                        .fillCoffeeTank(1000)
                        .build()
        );
        CompletableFuture.runAsync(myEventGenerator::connect, executor);
        return myEventGenerator.map(SavedEvent::getOccurredEvent);
    }

    public int getSizeQueue(){
        return myBlockingQueue.size();
    }
}
