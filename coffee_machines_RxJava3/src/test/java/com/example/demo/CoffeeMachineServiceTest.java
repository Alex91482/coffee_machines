package com.example.demo;

import com.example.demo.entity.CoffeeEvent;
import com.example.demo.entity.SavedEvent;
import com.example.demo.entity.beverages.TypesCoffeeEvent;
import com.example.demo.service.CoffeeMachineService;
import com.example.demo.util.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoffeeMachineServiceTest {

    //@Test
    /*public void testObservable(){
        try {
            IdGenerator idGenerator = new IdGenerator();
            CoffeeMachineService cms = new CoffeeMachineService();

            CoffeeEvent coffeeEvent = new CoffeeEvent(TypesCoffeeEvent.Americano);
            SavedEvent savedEvent = new SavedEvent(
                    idGenerator.generation(),
                    LocalDateTime.now(),
                    1000,
                    1000,
                    coffeeEvent);

            //cms.getCoffeeStreamTest(savedEvent).subscribe(System.out::println);
            Thread.sleep(6000);

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    //@Test
    public void testSwitch(){
        var control = "";
        var str = "Hi";
        switch (str){
            case "hi" -> control = "error";
            case "Hi" -> control = "success";
            case "haudiho" -> control = "error_ho";
            default -> control = "default";
        }
        assertEquals(control, "success");
    }
}
