package com.devkhoa.statuscollector.elasticsearch.config;

import com.devkhoa.statuscollector.configdata.ElasticSearchConfigData;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.net.URL;
import java.util.Objects;

@Configuration
@ComponentScan(basePackages = "com.devkhoa.statuscollector")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    public final ElasticSearchConfigData elasticSearchConfigData;

    public ElasticSearchConfig(ElasticSearchConfigData elasticSearchConfigData) {
        this.elasticSearchConfigData = elasticSearchConfigData;
    }

    @SneakyThrows
    @Override
    public RestHighLevelClient elasticsearchClient() {
        URL serverUri = new URL(elasticSearchConfigData.getConnectionUrl());
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(serverUri.getHost(), serverUri.getPort(), serverUri.getProtocol()))
                        .setRequestConfigCallback(
                        requestConfigBuilder ->
                                requestConfigBuilder
                                        .setConnectTimeout(elasticSearchConfigData.getConnectTimeoutMs())
                                        .setSocketTimeout(elasticSearchConfigData.getSocketTimeoutMs())
                )
        );
    }
}
