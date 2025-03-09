package com.example.kadastr.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//configuration of test profile
@Configuration
@ComponentScan("com.example.kadastr")
@Profile("test")
public class TestConfig {
}
