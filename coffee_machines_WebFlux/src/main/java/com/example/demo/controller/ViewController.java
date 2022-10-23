package com.example.demo.controller;

import com.example.demo.service.CoffeeAndWaterEnum;
import com.example.demo.service.CoffeeMachineService;
import com.example.demo.service.beverages.EnumBeverages;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

@Controller
public class ViewController {

    private final CoffeeMachineService coffeeMachineService;

    public ViewController(CoffeeMachineService coffeeMachineService){
        this.coffeeMachineService = coffeeMachineService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Mono<Rendering> getIndex(final Model model){
        //если направлять запросы на https а не на http то будет падать с ошибкой java.lang.IllegalArgumentException
        return Mono.just(Rendering.view("index1").build());
    }

    @GetMapping("/get-americano")
    public Mono<Rendering> getBeverageAmericano(final Model model){
        //отправляем сообщения о приготовленных напитках
        return Mono.just(
                Rendering.view("index1")
                        .modelAttribute("strs",
                                new ReactiveDataDriverContextVariable(coffeeMachineService.getBeverages(EnumBeverages.Americano), 1,1))
                        .build());
    }

    @GetMapping("/get-espresso")
    public Mono<Rendering> getBeverageEspresso(final Model model){
        //отправляем сообщения о приготовленных напитках
        return Mono.just(
                Rendering.view("index1")
                        .modelAttribute("strs",
                                new ReactiveDataDriverContextVariable(coffeeMachineService.getBeverages(EnumBeverages.Espresso), 1,1))
                        .build());
    }

    @GetMapping("/get-doubleespresso")
    public Mono<Rendering> getBeveragesDoubleEspresso(final Model model){

        return Mono.just(
                Rendering.view("index1")
                        .modelAttribute("strs",
                                new ReactiveDataDriverContextVariable(coffeeMachineService.getBeverages(EnumBeverages.DoubleEspresso), 1,1))
                        .build());
    }

    @GetMapping("/fill-water")
    public Mono<Rendering> setWater(){
        //
        return Mono.just(
                Rendering.view("index1")
                        .modelAttribute("strs",
                                new ReactiveDataDriverContextVariable(coffeeMachineService.setCoffeeAndWater(CoffeeAndWaterEnum.WATER), 1,1))
                        .build());
    }

    @GetMapping("/fill-coffee")
    public Mono<Rendering> setCoffee(){
        //
        return Mono.just(
                Rendering.view("index1")
                        .modelAttribute("strs",
                                new ReactiveDataDriverContextVariable(coffeeMachineService.setCoffeeAndWater(CoffeeAndWaterEnum.COFFEE), 1,1))
                        .build());
    }

    @GetMapping("/fill-coffee-and-water")
    public Mono<Rendering> setWaterAndCoffee(){
        //
        return Mono.just(
                Rendering.view("index1")
                        .modelAttribute("strs",
                                new ReactiveDataDriverContextVariable(coffeeMachineService.setCoffeeAndWater(CoffeeAndWaterEnum.COFFEE_AND_WATER), 1,1))
                        .build());
    }
}
