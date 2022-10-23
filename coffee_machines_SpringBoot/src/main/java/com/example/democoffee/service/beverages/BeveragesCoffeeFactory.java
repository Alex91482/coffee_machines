package com.example.democoffee.service.beverages;

import com.example.democoffee.service.beverages.beveragesImpl.Americano;
import com.example.democoffee.service.beverages.beveragesImpl.DoubleEspresso;
import com.example.democoffee.service.beverages.beveragesImpl.Espresso;
import org.springframework.stereotype.Service;

@Service
public class BeveragesCoffeeFactory {
    //что бы сделать расширение асортимента просто добавлением классов используем фабрику
    //в зависимости от того какой кофе будет запрошен такой экземпляр и создаем
    //если такого кофе в асортименте нет возвращаем null

    public AbstractCoffeeBeverages createCoffeeBeverages(String coffee){

        AbstractCoffeeBeverages abstractCoffeeBeverages = null;

        switch (coffee){
            case ("Americano") : abstractCoffeeBeverages = new Americano(); break;
            case ("Espresso") : abstractCoffeeBeverages = new Espresso(); break;
            case ("DoubleEspresso") : abstractCoffeeBeverages = new DoubleEspresso(); break;
        }

        return abstractCoffeeBeverages;
    }
}
