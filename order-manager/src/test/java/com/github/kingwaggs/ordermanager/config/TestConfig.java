package com.github.kingwaggs.ordermanager.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@TestConfiguration
@EnableEncryptableProperties
public class TestConfig {
}
