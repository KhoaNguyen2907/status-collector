package com.devkhoa.statuscollector.statusfetcher.init.impl;

import com.devkhoa.statuscollector.kafka.admin.client.KafkaAdminClient;
import com.devkhoa.statuscollector.statusfetcher.init.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StatusStreamInitializer implements StreamInitializer {
    private final KafkaAdminClient kafkaAdminClient;
    private final Logger logger = LoggerFactory.getLogger(StatusStreamInitializer.class);
    public StatusStreamInitializer(KafkaAdminClient kafkaAdminClient) {
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        logger.info("All topics and schema registry ready to function");
    }
}
