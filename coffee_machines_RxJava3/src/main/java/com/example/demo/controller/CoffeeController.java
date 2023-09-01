package com.example.demo.controller;

import com.example.demo.entity.beverages.TypesCoffeeEvent;
import com.example.demo.service.CoffeeMachineService;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoffeeController {

    private final CoffeeMachineService coffeeMachineService;

    public CoffeeController(CoffeeMachineService coffeeMachineService){
        this.coffeeMachineService = coffeeMachineService;
    }

    @GetMapping("/get-americano")
    public Observable<String> getAmericano(){
        return coffeeMachineService.getCoffeeStreamTest(TypesCoffeeEvent.Americano);
    }

    @GetMapping("/get-espresso")
    public Observable<String> getEspresso(){
        return coffeeMachineService.getCoffeeStreamTest(TypesCoffeeEvent.Espresso);
    }

    @GetMapping("/get-doubleespresso")
    public Observable<String> getDoubleEspresso(){
        return coffeeMachineService.getCoffeeStreamTest(TypesCoffeeEvent.DoubleEspresso);
    }

    @GetMapping("/fill-water")
    public Observable<String> getFillWater(){
        return coffeeMachineService.getCoffeeStreamTest(TypesCoffeeEvent.Filling_Water);
    }

    @GetMapping("/fill-coffee")
    public Observable<String> getFillCoffee(){
        return coffeeMachineService.getCoffeeStreamTest(TypesCoffeeEvent.Filling_Coffee);
    }

    @GetMapping("/fill-coffee-and-water")
    public Observable<String> getFillCoffeeAndWater(){
        return coffeeMachineService.getCoffeeStreamTest(TypesCoffeeEvent.Filling_Coffee_And_Water);
    }
}
