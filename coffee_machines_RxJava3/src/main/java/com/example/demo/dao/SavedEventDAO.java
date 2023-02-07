package com.example.demo.dao;

import com.example.demo.entity.SavedEvent;
import io.reactivex.rxjava3.core.Single;

public interface SavedEventDAO {

    Single<String> checkAndCreateTestDB();
    void save(SavedEvent savedEvent);
}
