package com.github.kingwaggs.ordermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OrderManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderManagerApplication.class, args);
    }
}

