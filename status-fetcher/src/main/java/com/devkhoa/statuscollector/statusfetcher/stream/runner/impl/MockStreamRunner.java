package com.devkhoa.statuscollector.statusfetcher.stream.runner.impl;

import com.devkhoa.statuscollector.statusfetcher.exception.StatusFetcherException;
import com.devkhoa.statuscollector.statusfetcher.listener.StatusListener;
import com.devkhoa.statuscollector.statusfetcher.stream.model.Status;
import com.devkhoa.statuscollector.statusfetcher.stream.runner.StreamRunner;
import com.devkhoa.statuscollector.configdata.StatusFetcherConfigData;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
@ConditionalOnProperty(name = "status-fetcher.enable-mock-status", havingValue = "true")
public class MockStreamRunner implements StreamRunner {
    private final Logger logger = LoggerFactory.getLogger(MockStreamRunner.class);
    private final StatusFetcherConfigData configData;
    private final StatusListener listener;
    private final ObjectMapper mapper;
    private final Random random = new Random();
    private final String[] words = new String[]{
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetuer",
            "adipiscing",
            "elit",
            "Maecenas",
            "porttitor",
            "congue",
            "massa",
            "Fusce",
            "posuere",
            "magna",
            "sed",
            "pulvinar",
            "ultricies",
            "purus",
            "lectus",
            "malesuada",
            "libero"
    };


    public MockStreamRunner(StatusFetcherConfigData configData, StatusListener listener, @Qualifier("streamMapper") ObjectMapper mapper){
        this.configData = configData;
        this.listener = listener;
        this.mapper = mapper;
    }

    @Override
    public void start() {
        String[] keywords = configData.getStatusKeywords().toArray(new String [0]);
        int minStatusLength = configData.getMockMinStatusLength();
        int maxStatusLength = configData.getMockMaxStatusLength();
        long sleepTimeMs = configData.getMockSleepMs();

        logger.info("Starting mock Status streams for keywords {}", Arrays.toString(keywords));
        simulateStatusStream(keywords, minStatusLength, maxStatusLength, sleepTimeMs);
    }

    private void simulateStatusStream(String[] keywords, int minStatusLength, int maxStatusLength, long sleepTimeMs) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String formattedStatusAsRawJson = getJsonFormattedStatus(keywords, minStatusLength, maxStatusLength);
                    Status status = mapper.readValue(formattedStatusAsRawJson, Status.class);
                    listener.onStatus(status);
                    sleep(sleepTimeMs);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private void sleep(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e){
            throw new StatusFetcherException("Error when sleeping");
        }
    }

    private String getJsonFormattedStatus(String[] keywords, int minStatusLength, int maxStatusLength) {
        String statusDateFormat = "MMM dd yyyy HH:mm:ss ";
        String[] params = new String[]{
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(statusDateFormat)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomStatusContent(keywords, minStatusLength, maxStatusLength),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };
        return formatStatusAsJsonWithParams(params);
    }

    private String formatStatusAsJsonWithParams(String[] params) {
        String status = "{" +
                    "\"created_at\":\"{0}\"," +
                    "\"id\":\"{1}\"," +
                    "\"text\":\"{2}\"," +
                    "\"user\":{\"id\":\"{3}\"}" +
                    "}";

        for (int i = 0; i < params.length; i++) {
            status = status.replace("{" + i + "}", params[i]);
        }
        return status;
    }

    private String getRandomStatusContent(String[] keywords, int minStatusLength, int maxStatusLength) {
        StringBuilder content = new StringBuilder();
        int statusLength = random.nextInt(maxStatusLength - minStatusLength + 1) + minStatusLength;
        return constructRandomStatus(keywords, content, statusLength);
    }

    private String constructRandomStatus(String[] keywords, StringBuilder content, int statusLength) {
        for (int i = 0; i < statusLength; i++) {
            content.append(words[random.nextInt(words.length)]).append(" ");
            if (i == statusLength / 2) {
                content.append(keywords[random.nextInt(keywords.length)]).append(" ");
            }
        }
        return content.toString().trim();
    }
}
