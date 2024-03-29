package com.devkhoa.statuscollector.statusloader.consumer;

import com.devkhoa.statuscollector.config.service.KafkaConsumer;
import com.devkhoa.statuscollector.configdata.KafkaConfigData;
import com.devkhoa.statuscollector.statusloader.converter.AvroToElasticStatusConverter;
import com.devkhoa.statuscollector.elasticsearch.client.service.impl.StatusElasticSearchIndexClient;
import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.statusloader.exception.StatusLoaderException;
import com.devkhoa.statuscollector.kafka.admin.client.KafkaAdminClient;
import com.devkhoa.statuscollector.kafka.avro.model.StatusAvroModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class is responsible for consuming Kafka messages of type StatusAvroModel.
 */
@Service
public class StatusKafkaConsumer implements KafkaConsumer<Long, StatusAvroModel> {
    private static final Logger LOG = LoggerFactory.getLogger(StatusKafkaConsumer.class);

    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final RetryTemplate retryTemplate;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final AvroToElasticStatusConverter converter;
    private final StatusElasticSearchIndexClient elasticSearchIndexClient;

    public StatusKafkaConsumer(KafkaAdminClient kafkaAdminClient, KafkaConfigData kafkaConfigData, RetryTemplate retryTemplate, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, AvroToElasticStatusConverter converter, StatusElasticSearchIndexClient elasticSearchIndexClient) {
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.retryTemplate = retryTemplate;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.converter = converter;
        this.elasticSearchIndexClient = elasticSearchIndexClient;
    }

    /**
     * This method is called when the application starts. It checks if the Kafka topics exist and starts the Kafka listener.
     *
     * @param event The application started event.
     * @throws StatusLoaderException if the Kafka topics do not exist.
     */
    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        try {
            LOG.info("Checking if the topic(s) exist");
            boolean isAllTopicsCreated = retryTemplate.execute(kafkaAdminClient::checkTopicsCreated);
            LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
            kafkaListenerEndpointRegistry.getListenerContainer("statusListener").start();
        } catch (Exception e) {
            throw new StatusLoaderException("Topic(s) not exist");
        }
    }

    /**
     * This method is called when a Kafka message is received. It converts the Avro messages to ElasticSearch index models and saves them to ElasticSearch.
     *
     * @param messages The received Avro messages.
     * @param keys The keys of the received messages.
     * @param partitions The partitions of the received messages.
     * @param offsets The offsets of the received messages.
     */
    @Override
    @KafkaListener(id="statusListener", idIsGroup = false, topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<StatusAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("{} number of message received with keys {}, partitions {} and offsets {}, " +
                        "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());

        List<StatusIndexModel> statusIndexModel = converter.getIndexModel(messages);
        LOG.info("Converted to StatusIndexModel: {}", statusIndexModel);

        elasticSearchIndexClient.save(statusIndexModel);
        LOG.info("Saved to elastic search");
    }
}