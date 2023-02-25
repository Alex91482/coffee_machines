package com.example.demo.entity;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Document
@Getter
public class SavedEvent {

    @Id
    private final long id;
    /**
     * время когда произошло событие
     */
    private final LocalDateTime eventTime;
    /**
     * уровень воды в баке
     */
    private final int fillTheWaterTank;
    /**
     * заполнение бака кофе
     */
    private final int fillCoffeeTank;
    /**
     * произошедшее событие
     */
    private final CoffeeEvent coffeeEvent;
    /**
     * максимальная вместимость для данной кофемашины
     * максимальный уровень воды в баке
     */
    @Transient
    private static final int maxWaterLevel = 1000;
    /**
     * максимальная вместимость для данной кофемашины
     * мксимльный уровень коффе
     */
    @Transient
    private static final int maxCoffeeLevel = 1000;

    public SavedEvent(long id, LocalDateTime eventTime, int fillTheWaterTank, int fillCoffeeTank, CoffeeEvent coffeeEvent){
        this.id = id;
        this.eventTime = eventTime;
        this.fillTheWaterTank = fillTheWaterTank;
        this.fillCoffeeTank = fillCoffeeTank;
        this.coffeeEvent = coffeeEvent;
    }
}
