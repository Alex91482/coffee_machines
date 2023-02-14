package com.example.demo.entity.beverages;

public enum TypesCoffeeEvent {

    Americano("Americano",120,10,"Americano is made from one or two shots of espresso, to which 30 to 470 ml of hot water is added. During preparation, hot water can be taken both from the espresso machine and from a separate kettle or heater."),
    DoubleEspresso("DoubleEspresso",100,20,"Double espresso, made from 18-22 grams of coffee, has a volume of 60 milliliters."),
    Espresso("Espresso",100,10,"Classic espresso usually has a volume of 25-35 milliliters and is served in a 60-70 milliliter demitasse cup or a special espresso cup.")
    ;

    private final String coffeeType;
    private final int waterLevel;
    private final int coffeeLevel;
    private final String description;

    TypesCoffeeEvent(String coffeeType, int waterLevel, int coffeeLevel, String description){
        this.coffeeType = coffeeType;
        this.description = description;
        this.waterLevel = waterLevel;
        this.coffeeLevel = coffeeLevel;
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public int getCoffeeLevel() {
        return coffeeLevel;
    }

    public String getDescription() {
        return description;
    }
}
