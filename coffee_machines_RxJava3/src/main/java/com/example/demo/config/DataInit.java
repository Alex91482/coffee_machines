package com.example.demo.config;

import com.example.demo.dao.SavedEventDAO;
import com.example.demo.dao.SavedEventDAOImpl;
import com.example.demo.entity.CoffeeEvent;
import com.example.demo.entity.SavedEvent;
import com.example.demo.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInit implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInit.class);

    private final SavedEventDAOImpl savedEventDAO;
    private final IdGenerator idGenerator;

    public DataInit(SavedEventDAOImpl savedEventDAO, IdGenerator idGenerator){
        this.savedEventDAO = savedEventDAO;
        this.idGenerator = idGenerator;
    }

    @Override
    public void run(ApplicationArguments args){

        /*savedEventDAO.checkAndCreateTestDB()
                .subscribe(
                        success -> {
                            SavedEvent savedEvent = new SavedEvent(
                                    idGenerator.generation(),
                                    LocalDateTime.now(),
                                    1000,
                                    1000,
                                    new CoffeeEvent()
                            );
                            savedEventDAO.save(savedEvent);
                            logger.info(success);
                        },
                        error -> logger.error("Exception {}", error.getMessage())
        );*/
    }
}
