package com.example.demo.entity.beverages;

public enum TypesCoffeeEvent {

    Americano("Americano"),
    DoubleEspresso("DoubleEspresso"),
    Espresso("Espresso")
    ;

    private final String coffeeType;

    TypesCoffeeEvent(String coffeeType){
        this.coffeeType = coffeeType;
    }

    public String getCoffeeType() {
        return coffeeType;
    }
}
