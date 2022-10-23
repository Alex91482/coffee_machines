package com.example.demo.service;

public enum CoffeeAndWaterEnum {
    COFFEE("Filling the coffee tank"),
    WATER("Filling the water tank"),
    COFFEE_AND_WATER("Filling the tank with coffee and water");

    CoffeeAndWaterEnum(String event){
        this.event = event;
    }
    private final String event;

    public String getEvent(){
        return event;
    }
}
