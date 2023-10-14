package com.example.demo;

import com.example.demo.service.BeveragesCoffeeFactory;
import com.example.demo.service.beverages.Americano;
import com.example.demo.service.beverages.DoubleEspresso;
import com.example.demo.service.beverages.EnumBeverages;
import com.example.demo.service.beverages.Espresso;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeveragesCoffeeFactoryTest {

    @Test
    public void factoryTest(){
        BeveragesCoffeeFactory beveragesCoffeeFactory = new BeveragesCoffeeFactory();
        var americano = beveragesCoffeeFactory.createCoffeeBeverages(EnumBeverages.Americano);
        var doubleEspresso = beveragesCoffeeFactory.createCoffeeBeverages(EnumBeverages.DoubleEspresso);
        var espresso = beveragesCoffeeFactory.createCoffeeBeverages(EnumBeverages.Espresso);

        assertTrue(americano instanceof Americano);
        assertTrue(doubleEspresso instanceof DoubleEspresso);
        assertTrue(espresso instanceof Espresso);
    }
}
