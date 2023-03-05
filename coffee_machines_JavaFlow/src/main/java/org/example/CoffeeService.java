package org.example;

import org.example.entity.CoffeeEvent;
import org.example.entity.SavedEvent;
import org.example.entity.TypesCoffeeEvent;
import org.example.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.concurrent.Flow.*;

public class CoffeeService {

    private final IdGenerator idGenerator = new IdGenerator();
    private final CoffeeSubscription coffeeSubscription = new CoffeeSubscription();


    public Publisher<SavedEvent> getCoffeeStream(CoffeeSubscriber coffeeSubscriber, TypesCoffeeEvent typesCoffeeEvent){
        coffeeSubscription.subscribeToReceive(coffeeSubscriber, createSavedEvent(typesCoffeeEvent));
        return subscriber -> subscriber.onSubscribe(coffeeSubscription);
    }

    private SavedEvent createSavedEvent(TypesCoffeeEvent typesCoffeeEvent){
        CoffeeEvent coffeeEvent = new CoffeeEvent(typesCoffeeEvent);
        return new SavedEvent(idGenerator.generation(), LocalDateTime.now(), 0, 0, coffeeEvent);
    }

    public void stop(){
        coffeeSubscription.stopExecutor();
    }
}
