package com.example.demo.repositories;

import com.example.demo.entity.SavedEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SavedEventRepository extends ReactiveMongoRepository<SavedEvent, Long> {
}
