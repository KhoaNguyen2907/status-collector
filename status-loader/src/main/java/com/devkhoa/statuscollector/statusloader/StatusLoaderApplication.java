package com.devkhoa.statuscollector.statusloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.devkhoa.statuscollector"})
public class StatusLoaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatusLoaderApplication.class, args);
    }
}
