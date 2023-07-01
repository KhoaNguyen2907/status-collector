package com.devkhoa.statuscollector.statusfetcher.listener;

import com.devkhoa.statuscollector.configdata.KafkaConfigData;
import com.devkhoa.statuscollector.kafka.avro.model.StatusAvroModel;
import com.devkhoa.statuscollector.kafka.producer.service.KafkaProducer;
import com.devkhoa.statuscollector.statusfetcher.converter.StatusToAvroConverter;
import com.devkhoa.statuscollector.statusfetcher.stream.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StatusListener {
    private final Logger logger = LoggerFactory.getLogger(StatusListener.class);
    private final KafkaProducer<Long, StatusAvroModel> kafkaProducer;
    private final KafkaConfigData kafkaConfigData;
    private final StatusToAvroConverter converter;

    public StatusListener(KafkaProducer<Long, StatusAvroModel> kafkaProducer, KafkaConfigData kafkaConfigData, StatusToAvroConverter converter) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaConfigData = kafkaConfigData;
        this.converter = converter;
    }

    public void onStatus(Status status){
        logger.info("Status with text: " + status.getText());
        StatusAvroModel model = converter.getStatusAvroModel(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), model.getUserId(), model);
    }
}
