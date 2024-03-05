package com.devkhoa.statuscollector.configdata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elastic-search-query")
@Data
public class ElasticSearchQueryConfigData {
    private String indexName;
    private String textField;
}
