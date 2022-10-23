package com.example.democoffee.controller;

import com.example.democoffee.entity.CoffeeMachineA.CoffeeMachineA;
import com.example.democoffee.service.CoffeeMachineA.NavigationBar;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="CoffeeMachineAController", description="REST контроллер отвечающий за команды для приготовление кофе")
@RestController
public class CoffeeMachineAController { //localhost:8080/swagger-ui.html

    @Value("${coffee.complete.message}")
    private String coffeeIsReady;               //кофе готово
    @Value("${coffee.notComplete.message}")
    private String notEnoughIngredients;        //проверьте количество ингредиентов и уровень воды
    @Value("${coffee.waterTank.message}")
    private String waterTankFull;               //бак с водой заполнен
    @Value("${coffee.coffeeTank.message}")
    private String coffeeTankFull;              //бак с кофе заполнен
    @Value("${coffee.fillError.message}")
    private String coffeeMachineFillError;      //ошибка при заполнении бака

    @Autowired
    NavigationBar navigationBar;

    @Operation(summary = "Приготовить американо", description = "Запрос к методу сервиса приготовитьАмерикано " +
            "на выходе получаем логическое значение хватило ли ингредиентов или нет и в зависимости от этого возвращаем http статус и сообщение соответствующее статусу")
    @GetMapping(value = "/getAmericano")
    public ResponseEntity<String> getAmericano() {             //приготовить америкона на выходе логическое значение и статус
        boolean a = navigationBar.prepareAmericano();
        return a
                ? new ResponseEntity<>(coffeeIsReady, HttpStatus.OK)
                : new ResponseEntity<>(notEnoughIngredients, HttpStatus.LOCKED);
    }

    @Operation(summary = "Приготовить эспрессо", description = "Запрос к методу сервиса приготовитьЭспрессо " +
            "на выходе получаем логическое значение хватило ли ингредиентов или нет и в зависимости от этого возвращаем http статус и сообщение соответствующее статусу")
    @GetMapping(value = "/getEspresso")
    public ResponseEntity<String> getEspresso() {              //приготовить эспрессо на выходе сообщение и статус
        boolean a = navigationBar.prepareEspresso();
        return a
                ? new ResponseEntity<>(coffeeIsReady, HttpStatus.OK)
                : new ResponseEntity<>(notEnoughIngredients, HttpStatus.LOCKED);
    }

    @Operation(summary = "Приготовить двойной эспрессо", description = "Запрос к методу сервиса приготовитьДвойнойЭспрессо " +
            "на выходе получаем логическое значение хватило ли ингредиентов или нет и в зависимости от этого возвращаем http статус и сообщение соответствующее статусу")
    @GetMapping(value = "/getDoubleEspresso")
    public ResponseEntity<String> getDoubleEspresso() {        //приготовить двойной эспрессо на выходе сообщение и статус
        boolean a = navigationBar.prepareDoubleEspresso();
        return a
                ? new ResponseEntity<>(coffeeIsReady, HttpStatus.OK)
                : new ResponseEntity<>(notEnoughIngredients, HttpStatus.LOCKED);
    }

    @Operation(summary = "Заполнить бак с водой", description = "Запрос к методу сервиса заполнитьБакСВодой " +
            "на выходе получаем логическое значение true если все выполнилось без ошибок и в зависимости от этого возвращаем http статус и сообщение соответствующее статусу")
    @GetMapping(value = "/upWaterTank")
    public ResponseEntity<String> getUpWaterTank() {        //заполнить бак водой на выходе сообщение и статус
        boolean a = navigationBar.fillTheWaterTank();
        return a
                ? new ResponseEntity<>(waterTankFull, HttpStatus.OK)
                : new ResponseEntity<>(coffeeMachineFillError,HttpStatus.LOCKED);
    }

    @Operation(summary = "Заполнить бак с кофе", description = "Запрос к методу сервиса заполнитьБакСКофе " +
            "на выходе получаем логическое значение true если все выполнилось без ошибок и в зависимости от этого возвращаем http статус и сообщение соответствующее статусу")
    @GetMapping(value = "/upCoffeeTank")
    public ResponseEntity<String> getUpCoffeeTank(){       //заполнить бак кофе на выходе сообщение и статус
        boolean a = navigationBar.fillCoffeeTank();
        return a
                ? new ResponseEntity<>(coffeeTankFull, HttpStatus.OK)
                : new ResponseEntity<>(coffeeMachineFillError,HttpStatus.LOCKED);
    }

    @Operation(summary = "Заполнить бак с водой и кофе одновременно", description = "Запрос к методу сервиса одновременноЗаполнитьБаки " +
            "на выходе получаем логическое значение true если все выполнилось без ошибок и в зависимости от этого возвращаем http статус и сообщение соответствующее статусу")
    @GetMapping(value = "/getFillAllTank")
    public ResponseEntity<String> getFillAll(){             //заполнить оба бака на выходе сообщение и статус
        boolean a = navigationBar.fillAll();
        return a
                ? new ResponseEntity<>(waterTankFull + " " + coffeeTankFull, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Получить инфомацию о текущем заполнении баков", description = "Запрос к методу сервиса информацияОКофеМашине " +
            "на выходе получаем строку где указан последний зарегистрированный уровень воды и кофе и в зависимости от этого возвращаем http статус и сообщение с количеством ингредиентов или 404")
    @GetMapping(value = "/getInfoCoffeeMachine")
    public ResponseEntity<String> getInfoCoffeeMachine(){   //получить информацию по наличаю кофе и воды на выходе сообщение и статус
        String a = navigationBar.showTheCurrentVolumeOfWaterAndCoffee();
        return a != null
                ? new ResponseEntity<>(a, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Получить список выполненых операций", description = "Запрос к методу сервиса получитьИнфо " +
            "на выходе получаем сообщение формата json со статистикой и возвращаем http статус с json либо статус 404")
    @PostMapping(value = "/getStatistics")                  //получить статистику на выходе json и статус
    public ResponseEntity<List<CoffeeMachineA>> getStatistics(){
        List<CoffeeMachineA> a = navigationBar.getInformation();
        return a.size() > 0
                ? new ResponseEntity<>(a, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
