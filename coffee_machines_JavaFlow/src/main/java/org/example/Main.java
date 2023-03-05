package org.example;

import org.example.entity.TypesCoffeeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final List<CompletableFuture<Void>> list = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start");

        CoffeeService coffeeService = new CoffeeService();
        createCoffeeStream(coffeeService, TypesCoffeeEvent.Americano);
        createCoffeeStream(coffeeService, TypesCoffeeEvent.Espresso);
        createCoffeeStream(coffeeService, TypesCoffeeEvent.DoubleEspresso);

        Thread.sleep(10000);

        for(CompletableFuture<Void> future : list){
            try {
                future.get(1000, TimeUnit.MILLISECONDS);
            }catch (Exception e){
                System.err.println("Time out exception: " + e.getMessage());
            }
        }
        coffeeService.stop();

        System.out.println("End");
    }

    private static void createCoffeeStream(CoffeeService coffeeService, TypesCoffeeEvent typesCoffeeEvent){
        var future = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": Start");
            CoffeeSubscriber coffeeSubscriber = new CoffeeSubscriber();
            coffeeService.getCoffeeStream(coffeeSubscriber, typesCoffeeEvent).subscribe(coffeeSubscriber);
        });
        list.add(future);
    }
}