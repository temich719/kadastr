package com.example.kadastr.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//configuration of prod profile
@Configuration
@Profile("prod")
@EnableCaching
public class ProdConfig {
}
