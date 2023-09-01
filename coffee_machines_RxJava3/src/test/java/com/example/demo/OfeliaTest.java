package com.example.demo;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class OfeliaTest {

    //@Test
    public void test1() throws InterruptedException{
        System.out.println(LocalDateTime.now());

        Observable<Boolean> timer = Observable.interval(2, TimeUnit.SECONDS)
                .flatMap(intrval -> toBeOrNotToBe().toObservable())
                .filter(element -> element);

        timer.take(1).subscribe(element -> System.out.println(LocalDateTime.now() + " " + element));

        Thread.sleep(4000);
    }

    private Single<Boolean> toBeOrNotToBe(){
        return Single.create(emitter -> {
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                emitter.onError(e);
            }
            emitter.onSuccess(true);
        });
    }
}
