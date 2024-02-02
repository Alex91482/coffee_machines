package org.example;

import org.example.entity.CoffeeEvent;
import org.example.entity.SavedEvent;
import org.example.entity.TypesCoffeeEvent;
import org.example.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.concurrent.Flow.*;

public class CoffeeService {

    private final IdGenerator idGenerator = new IdGenerator();
    private final CoffeeSubscription coffeeSubscription = CoffeeSubscription.getInstance();

    //тут должнен быть запас кофе и воды


    public Publisher<SavedEvent> getCoffeeStream(CoffeeSubscriber coffeeSubscriber, SavedEvent savedEvent){
        coffeeSubscription.subscribeToReceive(coffeeSubscriber, savedEvent);
        return subscriber -> subscriber.onSubscribe(coffeeSubscription);
    }

    public SavedEvent createSavedEvent(TypesCoffeeEvent typesCoffeeEvent){
        CoffeeEvent coffeeEvent = new CoffeeEvent(typesCoffeeEvent);
        return new SavedEvent(idGenerator.generation(), LocalDateTime.now(), 0, 0, coffeeEvent);
    }

    public void stop(){
        coffeeSubscription.stopExecutor();
    }
}
