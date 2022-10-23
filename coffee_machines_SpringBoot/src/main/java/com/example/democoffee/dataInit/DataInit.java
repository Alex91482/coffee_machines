package com.example.democoffee.dataInit;

import com.example.democoffee.entity.CoffeeMachineA.CoffeeMachineA;
import com.example.democoffee.repository.CoffeeMachineARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataInit implements ApplicationRunner {
    //при каждом новом запуске кофемашины заполняем баки с водой и кофе

    private CoffeeMachineARepository coffeeMachineARepository;

    @Autowired
    public DataInit(CoffeeMachineARepository coffeeMachineARepository){
        this.coffeeMachineARepository = coffeeMachineARepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Data init complete");
        coffeeMachineARepository.save(new CoffeeMachineA("Coffee Machine start",new Date(),1000,1000));

    }
}
