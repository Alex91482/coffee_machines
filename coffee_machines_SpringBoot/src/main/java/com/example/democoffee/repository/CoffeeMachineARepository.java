package com.example.democoffee.repository;

import com.example.democoffee.entity.CoffeeMachineA.CoffeeMachineA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeMachineARepository extends JpaRepository<CoffeeMachineA, Long> {

    @Query(value = "SELECT * FROM event ORDER BY event_date DESC LIMIT 1", nativeQuery = true)
    CoffeeMachineA getTheLatestEntry();                                                     //получить последнюю запись из бд

}

