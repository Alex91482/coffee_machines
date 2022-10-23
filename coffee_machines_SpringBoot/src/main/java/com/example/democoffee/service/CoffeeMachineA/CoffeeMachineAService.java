package com.example.democoffee.service.CoffeeMachineA;

import com.example.democoffee.entity.CoffeeMachineA.CoffeeMachineA;
import com.example.democoffee.repository.CoffeeMachineARepository;
import com.example.democoffee.service.beverages.AbstractCoffeeBeverages;
import com.example.democoffee.service.beverages.BeveragesCoffeeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CoffeeMachineAService implements NavigationBar{

    @Autowired
    private BeveragesCoffeeFactory beveragesCoffeeFactory;
    @Autowired
    private CoffeeMachineARepository coffeeMachineARepository;
    @Autowired
    private EntityManager entityManager;

    private CoffeeMachineA getTheLatestEntryCoffeeMachine(){ //метод отвечающий за получение последней записи из бд
        try{
            return coffeeMachineARepository.getTheLatestEntry(); //возвращаем CoffeeMachineA из которой будем брать уровень воды и кол-во кофе
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null; //записей не найденно
    }

    private boolean makingACoffeeDrink(String coffeeDrink){ //метод принимает название напитка и выполняет запросы к бд и расчитывает затраты на приготовление
        try {

            //часть отвечающая за запрос к бд чтобы узнать уровень воды и количество коффе
            CoffeeMachineA x = getTheLatestEntryCoffeeMachine();
            if(x == null){
                return false; //записи в бд не найдены
            }
            int coffee = x.getFillCoffeeTank();
            int water = x.getFillTheWaterTank();
            System.out.println("coffee = " + coffee + " water = " + water);

            //часть отвечающая за получение количества требуемых ингридиентов для напитка
            AbstractCoffeeBeverages beverages = beveragesCoffeeFactory.createCoffeeBeverages(coffeeDrink);
            int coffeeBev = beverages.getCoffeeConsumption();
            int waterBev = beverages.getWaterConsumption();
            System.out.println("beverages "+ coffeeDrink +" coffee = " + coffeeBev + " water = " + waterBev);

            //часть отвечающая за подсчет того хватит ли имеющихся запасов воды и кофе
            int subtractionCoffee = coffee - coffeeBev;
            int subtractionWater = water - waterBev;
            System.out.println("subtraction Coffee = " + subtractionCoffee + " subtraction Water = " + subtractionWater);

            //если всего хватило от сохраняем название напитка дату/время и уровни воды и кофе (из старого уровня вычитаем то что получили с фабрики)
            if(subtractionCoffee >= 0 && subtractionWater >= 0){
                CoffeeMachineA coffeeMachineA = new CoffeeMachineA(coffeeDrink, new Date(), subtractionWater,subtractionCoffee);
                coffeeMachineARepository.save(coffeeMachineA);
                System.out.println("Save in db success");

                return true; //возвращаем тру если напиток существует и хватило ингредиентов
            }

        }catch (Exception e){
            System.out.println(e.getMessage()); //ошибки отправляем в консоль
        }

        return false; //не хватило ингредиентов для изготовления напитка
    }

    @Override
    public boolean prepareAmericano() {     //приготовить американо
        //запрашиваем из бд уровень воды и наличие кофе
        //если бак с кофе и водой не пустые то кофе сварено
        //сохраняем событие в бд

        return makingACoffeeDrink("Americano"); //true если на кофе хватило ингредиентов false если не хватило
    }

    @Override
    public boolean prepareEspresso() {      //приготовить экспрессо
        //запрашиваем из бд уровень воды и наличие кофе
        //если бак с кофе и водой не пустые то кофе сварено
        //сохраняем событие в бд

        return makingACoffeeDrink("Espresso"); //true если на кофе хватило ингредиентов false если не хватило
    }

    @Override
    public boolean prepareDoubleEspresso() { //приготовить двойной экспрессо
        //запрашиваем уровень воды и кофе
        //если бак с кофе и водой не пустые то кофе сварено
        //сохраняем событие в бд

        return makingACoffeeDrink("DoubleEspresso"); //true если на кофе хватило ингредиентов false если не хватило
    }

    @Override
    public boolean fillTheWaterTank() {     //заполнить бак с водой
        //запрашиваем из бд уровень воды
        //если бак не полный то заполняем
        //сохраняем событие в бд

        try{
            CoffeeMachineA currentState = getTheLatestEntryCoffeeMachine(); //вызываем текущее сотояние кофеварки
            if(currentState != null) {
                coffeeMachineARepository.save(new CoffeeMachineA("Filling with Water ", new Date(), currentState.getMaxWaterLevel(), currentState.getFillCoffeeTank()));
                return true; //бак с водой заполнен до максимума, кофе осталось в прежнем количестве
            }else{
                System.out.println("The last state of the coffee machine was not found. Try to call the command to fill the water tank and the coffee tank at the same time.");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean fillCoffeeTank() {      //заполнить бак с кофе
        //запрашиваем из бд уровень воды
        //если бак не полный то заполняем
        //сохраняем событие в бд

        try{
            CoffeeMachineA currentState = getTheLatestEntryCoffeeMachine(); //вызываем текущее сотояние кофеварки
            if(currentState != null){
                coffeeMachineARepository.save(new CoffeeMachineA("Filling with Coffee ", new Date(), currentState.getFillTheWaterTank(),currentState.getMaxCoffeeLevel()));
                return true; //бак с кофе заполнен до максимума, количество воды осталось на прежнем уровне
            }else{
                System.out.println("The last state of the coffee machine was not found. Try to call the command to fill the water tank and the coffee tank at the same time.");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean fillAll(){   //одновремменое заполнение баков с ингредиентами
        try {
            coffeeMachineARepository.save(new CoffeeMachineA("Filling All ", new Date(), 1000, 1000));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    @Override
    public String showTheCurrentVolumeOfWaterAndCoffee() { //показать текущий объем воды и кофе
        //показываем текущей объем воды и кофе
        //запрашиваем из бд уровень воды и наличие кофе

        CoffeeMachineA x = getTheLatestEntryCoffeeMachine(); //получаем последнюю запись
        if(x == null){
            return "There are no entries. Execute the command to fill the water and coffee tanks."; //записи не найдены
        }
        return "Water Left: " + x.getFillTheWaterTank() + " (milliliters), Coffee Left: " + x.getFillCoffeeTank() + " (gram).";
    }

    @Override
    public List<CoffeeMachineA> getInformation() {        //информация о кофемашине
        //информация о кофемашине
        //запрашиваем из бд инфо по событиям за последнее включение

        try{
            return coffeeMachineARepository.findAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

}
