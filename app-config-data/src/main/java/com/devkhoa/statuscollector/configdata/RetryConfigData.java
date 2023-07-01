package com.devkhoa.statuscollector.configdata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfigData {

    private long initialIntervalMs;
    private long maxIntervalMs;
    private double multiplier;
    private int maxAttempts;
    private long sleepTimeMs;
}
