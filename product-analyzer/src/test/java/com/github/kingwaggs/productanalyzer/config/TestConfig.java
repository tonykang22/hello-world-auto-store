package com.github.kingwaggs.productanalyzer.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.github.kingwaggs.productanalyzer")
@EnableEncryptableProperties
public class TestConfig {
}