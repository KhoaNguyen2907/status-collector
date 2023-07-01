package com.devkhoa.statuscollector.configdata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "status-fetcher")
public class StatusFetcherConfigData {
    private List<String> statusKeywords;
    private boolean enableMockStatus;
    private long mockSleepMs;
    private int mockMinStatusLength;
    private int mockMaxStatusLength;
}
