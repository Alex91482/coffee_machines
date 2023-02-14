package com.example.demo;

import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoTest {

    //@Test
    public void test1() throws InterruptedException {
        getCache1().subscribe(element -> System.out.println(element.toString()));
        Thread.sleep(4000);
    }

    private Observable<Object> getCache(){
        //запрос в базу
        return Observable.just(new Object(),new Object(),new Object());
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private void update(){
        CompletableFuture.runAsync(() -> {
            //запрос к апи и обновление базы
        }, executorService);
    }

    private Observable<Object> getCache1(){
        return Observable.create(emitter -> {
            try{
                update();
                getCache().subscribe(emitter::onNext);
                emitter.onComplete();

            }catch (Exception e){
                System.err.println(e.getMessage());
                emitter.onError(e);
            }
        });
    }
}
