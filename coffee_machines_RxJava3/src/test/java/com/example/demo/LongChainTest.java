package com.example.demo;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class LongChainTest {

    //@Test
    public void test1() throws InterruptedException{
        AtomicBoolean flag = new AtomicBoolean(false);
        Observable<Integer> observable1 = Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Observable<Integer> observable2 = Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        DisposableObserver<List<Integer>> di = new DisposableObserver<>() {
            @Override
            public void onNext(@NonNull List<Integer> list) {
                if (list.get(0) >= 9) {
                    dispose();
                }
                System.out.println(list);
            }
            @Override
            public void onError(@NonNull Throwable e) {}
            @Override
            public void onComplete() {System.out.println("onComplete ");}
        };

        Observable<List<Integer>> observable = Observable.zip(observable1, observable2, Arrays::asList)
                .flatMap(this::incrementList)
                .flatMap(this::incrementList)
                .map(element -> {
                    try{
                        Thread.sleep(250);
                        if(element.get(0) >= 5) flag.set(true);
                        Thread.sleep(250);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    return incr(element);
                });

        CompletableFuture.runAsync(() -> {
            while (true){
                try{
                    Thread.sleep(10);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    break;
                }
                if(flag.get() || di.isDisposed()){
                    di.dispose();
                    break;
                }
            }
            System.out.println("listener down");
        });

        observable.subscribe(di);

        Thread.sleep(10000);
    }
    private Observable<List<Integer>> incrementList(List<Integer> list){
        return Observable.just(incr(list));
    }
    private List<Integer> incr(List<Integer> list){
        return list.stream()
                .map(i -> i + 1)
                .toList();
    }
}
