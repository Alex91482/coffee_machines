package org.example;

import org.example.entity.SavedEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Flow.*;

public class CoffeeSubscriber implements Subscriber<SavedEvent> {

    private final DateTimeFormatter form = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Subscription subscription;
    private final SavedEvent savedEvent;
    private SavedEvent currentEvent;

    public CoffeeSubscriber(SavedEvent savedEvent) {
        this.savedEvent = savedEvent;
    }

    public long getSaveEventId(){
        return savedEvent == null ? 0 : savedEvent.id();
    }

    public int getHashCodeLastSaveEvent(){
        return savedEvent == null ? 0 : savedEvent.hashCode();
    }


    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(SavedEvent item) {
        this.currentEvent = new SavedEvent(item.id(), item.eventTime(), item.fillTheWaterTank(), item.fillCoffeeTank(), item.coffeeEvent());
        System.out.println(LocalTime.now().format(form) + " " + Thread.currentThread().getName() + ": " + currentEvent.coffeeEvent().getTypesCoffeeEvent().getCoffeeType());
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println(LocalTime.now().format(form) + " " + Thread.currentThread().getName() + " Exception in thread " + ": " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println(LocalTime.now().format(form) + " " + Thread.currentThread().getName() + ": " + "Orders " + savedEvent.coffeeEvent().getTypesCoffeeEvent().getCoffeeType() + " are over");
    }
}
