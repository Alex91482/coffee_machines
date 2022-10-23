package com.example.demo.controller;

import com.example.demo.entity.SavedEvent;
import com.example.demo.service.CoffeeMachineService;
import com.example.demo.service.beverages.EnumBeverages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class StatisticViewController {

    private final CoffeeMachineService coffeeMachineService;

    public StatisticViewController(CoffeeMachineService coffeeMachineService){
        this.coffeeMachineService = coffeeMachineService;
    }

    @GetMapping("/get-latest-entry")
    public Mono<Rendering> getLatestEntryView(){
        return Mono.just(
                Rendering.view("index2")
                        .modelAttribute("savedEvent", coffeeMachineService.getMonoLastEvent())
                        .build());

    }

    @GetMapping("/get-all-record-start-machine")
    public Mono<Rendering> getStartMachineEventView(){
        return Mono.just(
                Rendering.view("index2")
                        .modelAttribute("savedEvent", coffeeMachineService.getByAllEventToStartMachine())
                        .build());
    }

    @GetMapping("/get-all-record-americano")
    public Mono<Rendering> getAmericanoEventView(){
        return Mono.just(
                Rendering.view("index2")
                        .modelAttribute("savedEvent", coffeeMachineService.getByAllEventToAmericano())
                        .build());
    }

    @GetMapping("/get-all-record-espresso")
    public Mono<Rendering> getEspressoEventView(){
        return Mono.just(
                Rendering.view("index2")
                        .modelAttribute("savedEvent", coffeeMachineService.getByAllEventToEspresso())
                        .build());
    }

    @GetMapping("/get-all-record-doubleespresso")
    public Mono<Rendering> getDoubleEspressoEventView(){
        return Mono.just(
                Rendering.view("index2")
                        .modelAttribute("savedEvent", coffeeMachineService.getByAllEventToDoubleEspresso())
                        .build());
    }

    @GetMapping("/get-size")
    public ResponseEntity<String> getSize(){
        return new ResponseEntity<>(String.valueOf(coffeeMachineService.getSizeQueue()), HttpStatus.OK);
    }
}
