package com.example.democoffee.service.CoffeeMachineA;

import com.example.democoffee.entity.CoffeeMachineA.CoffeeMachineA;

import java.util.List;

public interface NavigationBar {

    String showTheCurrentVolumeOfWaterAndCoffee();                  //показать текущий объем воды и кофе
    List<CoffeeMachineA> getInformation();                          //информация о статистике использования
    boolean prepareAmericano();        //приготовить американо
    boolean prepareEspresso();         //приготовить экспрессо
    boolean prepareDoubleEspresso();   //приготовить двойной экспрессо
    boolean fillTheWaterTank();        //заполнить бак с водой
    boolean fillCoffeeTank();          //заполнить бак с кофе
    boolean fillAll();                  //заполнить оба бака

}
