package com.devkhoa.statuscollector.statusfetcher.stream.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean(name = "streamMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
