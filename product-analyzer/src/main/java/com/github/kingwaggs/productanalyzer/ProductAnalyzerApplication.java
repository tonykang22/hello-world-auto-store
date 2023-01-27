package com.github.kingwaggs.productanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProductAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductAnalyzerApplication.class, args);
    }

}
