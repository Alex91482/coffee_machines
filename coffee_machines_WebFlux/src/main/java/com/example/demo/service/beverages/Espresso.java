package com.example.demo.service.beverages;

public class Espresso extends AbstractCoffeeBeverages implements Beverages {

    private final int waterConsumption = 100; // 100 мл воды
    private final int coffeeConsumption = 10; // 10 гр кофе

    @Override
    public int getWaterConsumption(){
        return waterConsumption;
    }

    @Override
    public int getCoffeeConsumption(){
        return coffeeConsumption;
    }
}