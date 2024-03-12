package com.devkhoa.statuscollector.statusretriever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.devkhoa.statuscollector")
public class StatusRetrieverApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatusRetrieverApplication.class, args);
    }
}
