package com.example.demo.dao;

import com.example.demo.entity.SavedEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SavedEventRepository extends ReactiveMongoRepository<SavedEvent, Long> {
}
