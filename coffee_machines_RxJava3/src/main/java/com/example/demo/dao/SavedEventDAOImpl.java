package com.example.demo.dao;

import com.example.demo.entity.SavedEvent;
import com.example.demo.dao.repositories.SavedEventRepository;
import com.example.demo.util.IdGenerator;
import com.mongodb.reactivestreams.client.MongoClients;
import io.reactivex.rxjava3.core.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SavedEventDAOImpl implements SavedEventDAO{

    private final SavedEventRepository savedEventRepository;
    private final IdGenerator idGenerator;

    public SavedEventDAOImpl(SavedEventRepository savedEventRepository, IdGenerator idGenerator){
        this.savedEventRepository = savedEventRepository;
        this.idGenerator = idGenerator;
    }

    private static final Logger logger = LoggerFactory.getLogger(SavedEventDAOImpl.class);

    @Override
    public Single<SavedEvent> findFirstByOrderByEventTimeDesc(){
        return savedEventRepository.findFirstByOrderByEventTimeDesc();
    }

    @Override
    public Single<String> checkAndCreateTestDB(){
        return Single.create(event -> {
            try (var mongoClient = MongoClients.create()) {

                mongoClient.getDatabase("testdb");
                event.onSuccess("testdb create");

            }catch (Exception e){
                logger.error("Error to check and create: {}", e.getMessage());
                event.onError(e);
            }
        });
    }

    @Override
    public void save(SavedEvent savedEvent) {
        savedEventRepository.save(savedEvent).subscribe();
    }
}
