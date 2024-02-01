package org.example;

import org.example.entity.TypesCoffeeEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;

public class Main {

    private static final ArrayBlockingQueue<CompletableFuture<Void>> queue = new ArrayBlockingQueue<>(100, true);

    public static void main(String[] args) {
        System.out.println("Start");

        CoffeeService coffeeService = new CoffeeService();
        createCoffeeStream(coffeeService, TypesCoffeeEvent.Americano);
        createCoffeeStream(coffeeService, TypesCoffeeEvent.Espresso);
        createCoffeeStream(coffeeService, TypesCoffeeEvent.DoubleEspresso);

        while(!queue.isEmpty()){
            try {
                queue.poll().get();
            }catch (Exception e){
                System.err.println("Exception: " + e.getMessage());
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
        queue.add(future);
    }
}