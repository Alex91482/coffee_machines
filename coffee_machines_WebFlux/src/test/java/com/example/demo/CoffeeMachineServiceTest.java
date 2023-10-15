package com.example.demo;

import com.example.demo.dao.SavedEventDAOImpl;
import com.example.demo.entity.SavedEvent;
import com.example.demo.service.BeveragesCoffeeFactory;
import com.example.demo.service.CoffeeMachineService;
import com.example.demo.service.beverages.EnumBeverages;
import com.example.demo.util.idgenerator.IdGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

public class CoffeeMachineServiceTest {

    private final IdGenerator idGenerator = new IdGenerator();
    private final BeveragesCoffeeFactory beveragesCoffeeFactory = new BeveragesCoffeeFactory();
    private final SavedEventDAOImpl savedEventDAO = Mockito.mock(SavedEventDAOImpl.class);

    @Test
    public void setCoffeeTest(){

        Mockito.when(savedEventDAO.getTheLatestEntry()).thenReturn(Mono.just(
                new SavedEvent().builder()
                        .id(idGenerator.getRandomId())
                        .occurredEvent("Coffee Machine start")
                        .eventTime(LocalDateTime.now())
                        .fillTheWaterTank(1000)
                        .fillCoffeeTank(1000)
                        .build()
        ));
        CoffeeMachineService coffeeMachineService = new CoffeeMachineService(savedEventDAO, beveragesCoffeeFactory, idGenerator);

        StepVerifier.create(coffeeMachineService.getBeverages(EnumBeverages.Americano))
                .expectNext("Americano")
                .expectComplete()
                .verify();
        StepVerifier.create(coffeeMachineService.getBeverages(EnumBeverages.DoubleEspresso))
                .expectNext("DoubleEspresso")
                .expectComplete()
                .verify();
        StepVerifier.create(coffeeMachineService.getBeverages(EnumBeverages.Espresso))
                .expectNext("Espresso")
                .expectComplete()
                .verify();
    }

    @Test
    public void missingIngredientsTest(){
        Mockito.when(savedEventDAO.getTheLatestEntry()).thenReturn(Mono.just(
                new SavedEvent().builder()
                        .id(idGenerator.getRandomId())
                        .occurredEvent("Coffee Machine start")
                        .eventTime(LocalDateTime.now())
                        .fillTheWaterTank(100)
                        .fillCoffeeTank(100)
                        .build()
        ));
        CoffeeMachineService coffeeMachineService = new CoffeeMachineService(savedEventDAO, beveragesCoffeeFactory, idGenerator);

        StepVerifier.create(coffeeMachineService.getBeverages(EnumBeverages.Americano))
                .expectNext("Not enough ingredients. Fill the tank with coffee and water.")
                .expectComplete()
                .verify();
    }
}
