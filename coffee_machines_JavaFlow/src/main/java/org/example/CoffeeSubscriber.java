package org.example;

import org.example.entity.SavedEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Flow.*;

public class CoffeeSubscriber implements Subscriber<SavedEvent> {

    private final DateTimeFormatter form = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Subscription subscription;
    private SavedEvent savedEvent;

    public int getHashCodeLastSaveEvent(){
        if(savedEvent == null){
            return 0;
        }else{
            return savedEvent.hashCode();
        }
    }


    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(SavedEvent item) {
        savedEvent = item;
        System.out.println(LocalTime.now().format(form) + " " + Thread.currentThread().getName() + ": " + item.coffeeEvent().getTypesCoffeeEvent().getCoffeeType());
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println(LocalTime.now().format(form) + " " + Thread.currentThread().getName() + ": " + "Orders are over");
    }
}
