package com.example.demo.entity;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Запись о состоянии кофемашины
 */
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedEvent {

    @Id
    @Getter @Setter
    private long id;

    /**
     * произошедшее событие
     */
    @Getter @Setter
    private String occurredEvent;

    /**
     * время когда произошло событие
     */
    @Getter @Setter
    private LocalDateTime eventTime;

    /**
     * уровень воды в баке
     */
    @Getter @Setter
    private int fillTheWaterTank;

    /**
     * заполнение бака кофе
     */
    @Getter @Setter
    private int fillCoffeeTank;

    /**
     * максимальная вместимость для данной кофемашины
     * максимальный уровень воды в баке
     */
    @Transient
    @Getter
    private static final int maxWaterLevel = 1000;
    /**
     * максимальная вместимость для данной кофемашины
     * мксимльный уровень коффе
     */
    @Transient
    @Getter
    private static final int maxCoffeeLevel = 1000;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SavedEvent that = (SavedEvent) o;

        if (id != that.id) return false;
        return Objects.equals(eventTime, that.eventTime);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Saved Event = [ id = " + id +
                ", occurred Event = " + occurredEvent +
                ", event time = " + eventTime +
                ", fill the water tank = " + fillTheWaterTank +
                ", fill the coffee tank = " + fillCoffeeTank +
                " ]";
    }
}
