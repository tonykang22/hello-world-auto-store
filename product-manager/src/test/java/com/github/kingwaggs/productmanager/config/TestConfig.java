package com.github.kingwaggs.productmanager.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.github.kingwaggs.productmanager")
@EnableEncryptableProperties
public class TestConfig {
}