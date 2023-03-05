package org.example.util;

import java.util.Random;

public class IdGenerator {

    private static final Random random = new Random();

    public long generation(){
        return random.nextLong();
    }
}
