package org.example.entity;



import java.time.LocalDateTime;

/**
 * @param eventTime        время когда произошло событие
 * @param fillTheWaterTank уровень воды в баке
 * @param fillCoffeeTank   заполнение бака кофе
 * @param coffeeEvent      произошедшее событие
 */
public record SavedEvent(long id, LocalDateTime eventTime, int fillTheWaterTank, int fillCoffeeTank, CoffeeEvent coffeeEvent) {

    /**
     * максимальная вместимость для данной кофемашины
     * максимальный уровень воды в баке
     */
    private static final int maxWaterLevel = 1000;
    /**
     * максимальная вместимость для данной кофемашины
     * мксимльный уровень коффе
     */
    private static final int maxCoffeeLevel = 1000;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SavedEvent saveEvent = (SavedEvent) o;

        if (id != saveEvent.id) return false;
        if (fillTheWaterTank != saveEvent.fillTheWaterTank) return false;
        if (fillCoffeeTank != saveEvent.fillCoffeeTank) return false;
        return eventTime.equals(saveEvent.eventTime);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + eventTime.hashCode();
        result = 31 * result + fillTheWaterTank;
        result = 31 * result + fillCoffeeTank;
        return result;
    }
}
