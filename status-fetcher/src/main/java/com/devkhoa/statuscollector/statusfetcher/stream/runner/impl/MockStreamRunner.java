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

/**
 * This class is responsible for simulating a stream of statuses.
 * It is only active when the property "status-fetcher.enable-mock-status" is set to true.
 */
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

    /**
     * Starts the simulation of the status stream.
     */
    @Override
    public void start() {
        String[] keywords = configData.getStatusKeywords().toArray(new String [0]);
        int minStatusLength = configData.getMockMinStatusLength();
        int maxStatusLength = configData.getMockMaxStatusLength();
        long sleepTimeMs = configData.getMockSleepMs();

        logger.info("Starting mock Status streams for keywords {}", Arrays.toString(keywords));
        simulateStatusStream(keywords, minStatusLength, maxStatusLength, sleepTimeMs);
    }

    /**
     * Simulates a stream of statuses.
     * @param keywords Keywords to include in the statuses.
     * @param minStatusLength Minimum length of a status.
     * @param maxStatusLength Maximum length of a status.
     * @param sleepTimeMs Time to sleep between statuses.
     */
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

    /**
     * Sleeps for a specified amount of time.
     * @param sleepTimeMs Time to sleep in milliseconds.
     */
    private void sleep(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e){
            throw new StatusFetcherException("Error when sleeping");
        }
    }

    /**
     * Generates a JSON formatted status.
     * @param keywords Keywords to include in the status.
     * @param minStatusLength Minimum length of the status.
     * @param maxStatusLength Maximum length of the status.
     * @return The status as a JSON string.
     */
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

    /**
     * Formats a status as a JSON string.
     * @param params Parameters to include in the status.
     * @return The status as a JSON string.
     */
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

    /**
     * Generates a random status content.
     * @param keywords Keywords to include in the status.
     * @param minStatusLength Minimum length of the status.
     * @param maxStatusLength Maximum length of the status.
     * @return The status content as a string.
     */
    private String getRandomStatusContent(String[] keywords, int minStatusLength, int maxStatusLength) {
        StringBuilder content = new StringBuilder();
        int statusLength = random.nextInt(maxStatusLength - minStatusLength + 1) + minStatusLength;
        return constructRandomStatus(keywords, content, statusLength);
    }

    /**
     * Constructs a random status content.
     * @param keywords Keywords to include in the status.
     * @param content StringBuilder to build the status content.
     * @param statusLength Length of the status.
     * @return The status content as a string.
     */
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