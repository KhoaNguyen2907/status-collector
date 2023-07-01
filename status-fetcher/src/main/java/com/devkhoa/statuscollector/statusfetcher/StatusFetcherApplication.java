package com.devkhoa.statuscollector.statusfetcher;

import com.devkhoa.statuscollector.statusfetcher.stream.runner.StreamRunner;
import com.devkhoa.statuscollector.statusfetcher.init.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.devkhoa.statuscollector"})
public class StatusFetcherApplication implements CommandLineRunner {
    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;
    private final Logger logger = LoggerFactory.getLogger(StatusFetcherApplication.class);

    public StatusFetcherApplication(StreamRunner streamRunner, StreamInitializer streamInitializer){
        this.streamRunner = streamRunner;
        this.streamInitializer = streamInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(StatusFetcherApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        logger.info("App started");
        streamInitializer.init();
        streamRunner.start();
    }
}
