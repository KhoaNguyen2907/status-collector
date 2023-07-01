package com.devkhoa.statuscollector.kafka.producer.service.impl;

import com.devkhoa.statuscollector.kafka.avro.model.StatusAvroModel;
import com.devkhoa.statuscollector.kafka.producer.service.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@Service
public class StatusKafkaProducer implements KafkaProducer<Long, StatusAvroModel> {
    private static final Logger logger = LoggerFactory.getLogger(StatusKafkaProducer.class);
    private final KafkaTemplate<Long, StatusAvroModel> kafkaTemplate;

    public StatusKafkaProducer(KafkaTemplate<Long, StatusAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, Long key, StatusAvroModel message) {
        logger.info("Sending message = '{}' to topic '{}'.", message, topicName);
        ListenableFuture<SendResult<Long, StatusAvroModel>> future =  kafkaTemplate.send(topicName, key, message);
        addCallback(topicName, message, future);
        logger.info("Received ListenableFuture");
        logger.info("Message sent");
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            kafkaTemplate.destroy();
        }
    }

    private void addCallback(String topicName, StatusAvroModel message, ListenableFuture<SendResult<Long, StatusAvroModel>> future) {
        future.addCallback(new ListenableFutureCallback<SendResult<Long, StatusAvroModel>>() {
            @Override
            public void onFailure(Throwable ex) {
                logger.error("Error while sending message {} to topic {}", message, topicName);
            }

            @Override
            public void onSuccess(SendResult<Long, StatusAvroModel> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                logger.debug("Received new meta data. Topic: {}; Partition: {}; Offset: {}; Timestamp: {}; at time {}",
                        metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp(), System.currentTimeMillis());
            }
        });
    }
}
