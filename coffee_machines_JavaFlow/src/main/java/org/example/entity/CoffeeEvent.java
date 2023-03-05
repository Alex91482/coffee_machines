package org.example.entity;


import org.example.entity.beverages.AbstractCoffeeBeverages;
import org.example.entity.beverages.Beverages;

public class CoffeeEvent extends AbstractCoffeeBeverages implements Beverages {

    private final TypesCoffeeEvent typesCoffeeEvent;

    public CoffeeEvent(TypesCoffeeEvent typesCoffeeEvent){
        this.typesCoffeeEvent = typesCoffeeEvent;
    }

    public TypesCoffeeEvent getTypesCoffeeEvent() {
        return typesCoffeeEvent;
    }
}
