package com.example.demo.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class IdGenerator {

    private static final Random random = new Random();

    public long generation(){
        return random.nextLong();
    }
}
