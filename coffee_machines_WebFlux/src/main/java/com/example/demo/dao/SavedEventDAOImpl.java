package com.example.demo.dao;


import com.example.demo.entity.SavedEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SavedEventDAOImpl implements SavedEventDAO{

    private final SavedEventRepository savedEventRepository;

    public SavedEventDAOImpl(SavedEventRepository savedEventRepository){
        this.savedEventRepository = savedEventRepository;
    }

    public void save(SavedEvent savedEvent){
        savedEventRepository.save(savedEvent).subscribe();
    }

    public Mono<SavedEvent> findById(Long id){
        return savedEventRepository.findById(id);
    }

    public Flux<SavedEvent> findAll(){
        return savedEventRepository.findAll();
    }

    public Flux<SavedEvent> findByOccurredName(String name){
        return savedEventRepository.findByOccurredEvent(name);
    }

    public Mono<SavedEvent> update(SavedEvent savedEvent){
        return savedEventRepository.save(savedEvent);
    }

    public Mono<Void> delete(Long id){
        return savedEventRepository.deleteById(id);
    }

    public Mono<SavedEvent> getTheLatestEntry(){
        return savedEventRepository.findFirstByOrderByEventTimeDesc();
    }
}
