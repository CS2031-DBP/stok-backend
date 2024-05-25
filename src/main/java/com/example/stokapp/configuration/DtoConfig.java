package com.example.stokapp.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DtoConfig {
    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}