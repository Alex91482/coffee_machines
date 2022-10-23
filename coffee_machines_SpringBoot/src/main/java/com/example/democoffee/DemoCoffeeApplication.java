package com.example.democoffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DemoCoffeeApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DemoCoffeeApplication.class, args);
    }

}
