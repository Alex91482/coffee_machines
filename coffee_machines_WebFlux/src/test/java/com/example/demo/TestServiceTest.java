package com.example.demo;

import com.example.demo.service.beverages.EnumBeverages;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TestServiceTest {

    //@Test
    public void test(){
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println(LocalTime.now().format(dtf) + " Start test");

            TestService testService = new TestService();

            testService.getBeverages(EnumBeverages.Americano).subscribe(
                    value -> System.out.println(LocalTime.now().format(dtf) + " consumer 1: " + value),
                    e -> System.err.println("Achtung!!!!! " + e),
                    () -> System.out.println(LocalTime.now().format(dtf) + " consumer 1: complete")
            );

            Thread.sleep(3000);
            //System.out.println(testService.getSizeQueue());

            testService.getBeverages(EnumBeverages.Espresso)
                    .doOnSubscribe(i -> System.out.println(LocalTime.now().format(dtf) + " consumer 2: i am subscribe"))
                    .subscribe(
                            value -> System.out.println(LocalTime.now().format(dtf) + " consumer 2: " + value),
                            e -> System.err.println("Achtung!!!!! " + e),
                            () -> System.out.println(LocalTime.now().format(dtf) + " consumer 2: complete")
                    );

            System.out.println(testService.getSizeQueue());
            Thread.sleep(10_000);
            System.out.println(testService.getSizeQueue());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
