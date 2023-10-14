package com.example.demo.service;

import com.example.demo.dao.SavedEventDAOImpl;
import com.example.demo.entity.SavedEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StatisticService {

    private final SavedEventDAOImpl savedEventDAOImpl;

    public StatisticService(SavedEventDAOImpl savedEventDAOImpl){
        this.savedEventDAOImpl = savedEventDAOImpl;
    }

    public Flux<SavedEvent> getByAllEventToStartMachine(){
        return savedEventDAOImpl.findByOccurredName("Coffee Machine start");
    }

    public Flux<SavedEvent> getByAllEventToAmericano(){
        return savedEventDAOImpl.findByOccurredName("Americano");
    }

    public Flux<SavedEvent> getByAllEventToEspresso(){
        return savedEventDAOImpl.findByOccurredName("Espresso");
    }

    public Flux<SavedEvent> getByAllEventToDoubleEspresso(){
        return savedEventDAOImpl.findByOccurredName("DoubleEspresso");
    }
}
