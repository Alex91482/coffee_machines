package com.example.demo;

import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

public class DuplicationRequestTest {

    //@Test
    public void test1() {
        try {

            Observable<Integer> ob = Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
