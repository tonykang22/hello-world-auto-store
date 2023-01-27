package com.github.kingwaggs.productmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProductManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductManagerApplication.class, args);
    }
}

