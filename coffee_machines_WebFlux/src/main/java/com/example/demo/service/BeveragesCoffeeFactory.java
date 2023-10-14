package com.example.demo.service;


import com.example.demo.service.beverages.*;

import org.springframework.stereotype.Service;

/**
 * что бы сделать расширение асортимента просто добавлением классов используем фабрику
 * в зависимости от того какой кофе будет запрошен такой экземпляр и создаем
 */
@Service
public class BeveragesCoffeeFactory {

    public AbstractCoffeeBeverages createCoffeeBeverages(EnumBeverages beverages){

        return switch (beverages) {
            case Americano -> new Americano();
            case Espresso -> new Espresso();
            case DoubleEspresso -> new DoubleEspresso();
        };
    }
}
