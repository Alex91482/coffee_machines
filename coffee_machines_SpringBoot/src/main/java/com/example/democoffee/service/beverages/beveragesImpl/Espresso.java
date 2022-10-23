package com.example.democoffee.service.beverages.beveragesImpl;

import com.example.democoffee.service.beverages.AbstractCoffeeBeverages;
import com.example.democoffee.service.beverages.Beverages;
import org.springframework.stereotype.Component;


public class Espresso extends AbstractCoffeeBeverages implements Beverages {

    public Espresso(){
        final int waterConsumption = getWaterConsumption();
        final int coffeeConsumption = getCoffeeConsumption();
    }



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
