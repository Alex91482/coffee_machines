package com.example.demo.config;


import com.example.demo.dao.SavedEventDAOImpl;
import com.example.demo.entity.SavedEvent;
import com.example.demo.util.idgenerator.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInit implements ApplicationRunner {
    //при каждом новом запуске кофемашины заполняем баки с водой и кофе

    private static final Logger logger = LoggerFactory.getLogger(DataInit.class);
    private final SavedEventDAOImpl savedEventDAOImpl;
    private final IdGenerator idGenerator;

    @Autowired
    public DataInit(SavedEventDAOImpl savedEventDAOImpl, IdGenerator idGenerator){
        this.savedEventDAOImpl = savedEventDAOImpl;
        this.idGenerator = idGenerator;
    }

    @Override
    public void run(ApplicationArguments args){

        savedEventDAOImpl.save(new SavedEvent().builder()
                .id(idGenerator.getRandomId())
                .occurredEvent("Coffee Machine start")
                .eventTime(LocalDateTime.now())
                .fillTheWaterTank(1000)
                .fillCoffeeTank(1000)
                .build()
        );

        logger.info("Data init complete.");
    }
}
