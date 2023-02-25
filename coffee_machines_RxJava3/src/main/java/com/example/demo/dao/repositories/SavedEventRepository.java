package com.example.demo.dao.repositories;

import com.example.demo.entity.SavedEvent;

import io.reactivex.rxjava3.core.Single;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface SavedEventRepository extends RxJava3CrudRepository<SavedEvent, Long> {

    Single<SavedEvent> findFirstByOrderByEventTimeDesc(); //получить последнее событие по дате
}
