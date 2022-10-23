package com.example.democoffee.entity.CoffeeMachineA;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import java.util.Date;

@Schema(description = "Сущность событий кофемашины CoffeeMachineA")
@Entity
@Table(name = "event", schema = "public")
public class CoffeeMachineA{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    @Column(name = "Id", nullable = false, updatable = false)
    private long Id;                    //айди записи состояния

    @Schema(description = "Название события которое будем регистрировать", example = "Americano")
    @Column(name = "occurred_event", length = 64, nullable = false)
    @Getter @Setter
    private String occurredEvent;       //произошедшее событие

    @Schema(description = "Дата и время в которое произошло событие")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_date", nullable = false)
    @Getter @Setter
    private Date eventTime;             //время когда произошло событие

    @Schema(description = "Уровень воды в баке формат int, имеет ограничения 0 <= waterLevel <=1000")
    @DecimalMax(message = "The water level cannot be more than 1000", value = "1000")
    @DecimalMin(message = "The water level cannot be less than 0", value = "0")
    @Column(name = "water_fill_level")
    @Getter @Setter
    private int fillTheWaterTank;       //уровень воды в баке

    @Schema(description = "Уровень кофе в баке формат int, имеет ограничения 0 <= coffeeLevel <=1000")
    @DecimalMax(message = "The coffee level cannot be more than 1000", value = "1000")
    @DecimalMin(message = "The coffe level cannot be less than 0", value = "0")
    @Column(name = "coffee_fill_level")
    @Getter @Setter
    private int fillCoffeeTank;         //заполнение бака кофе

    public CoffeeMachineA(){}

    public CoffeeMachineA(String occurredEvent, Date eventTime, int fillTheWaterTank, int fillCoffeeTank){
        this.occurredEvent = occurredEvent;
        this.eventTime = eventTime;
        this.fillTheWaterTank = fillTheWaterTank;
        this.fillCoffeeTank = fillCoffeeTank;
    }

    //максимальная вместимость для данной кофемашины
    @Getter
    private static final int maxWaterLevel = 1000; // поскольку поле имеют модефикотор static final то по умолчанию @Transient
    @Getter
    private static final int maxCoffeeLevel = 1000; // поскольку поле имеют модефикотор static final то по умолчанию @Transient
}
