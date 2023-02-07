package com.example.demo.entity;

import com.example.demo.entity.beverages.AbstractCoffeeBeverages;
import com.example.demo.entity.beverages.Beverages;
import com.example.demo.entity.beverages.TypesCoffeeEvent;
import lombok.Getter;

@Getter
public class CoffeeEvent extends AbstractCoffeeBeverages implements Beverages {


    private final TypesCoffeeEvent typesCoffeeEvent;

    public CoffeeEvent(TypesCoffeeEvent typesCoffeeEvent){
        this.typesCoffeeEvent = typesCoffeeEvent;
    }
}
