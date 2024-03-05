package com.devkhoa.statuscollector.config;

import com.devkhoa.statuscollector.configdata.KafkaConfigData;
import com.devkhoa.statuscollector.configdata.KafkaConsumerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * KafkaConsumerConfig is a configuration class for Kafka consumers.
 * It provides beans for consumer configuration, consumer factory, and Kafka listener container factory.
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig<K extends Serializable, V extends SpecificRecordBase> {
    private KafkaConsumerConfigData kafkaConsumerConfigData;
    private KafkaConfigData kafkaConfigData;

    public KafkaConsumerConfig ( KafkaConsumerConfigData kafkaConsumerConfigData, KafkaConfigData kafkaConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
    }

    /**
     * Provides a map of consumer configuration properties.
     * @return A map of consumer configuration properties.
     */
    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        // The list of host:port pairs used for establishing the initial connections to the Kafka cluster.
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        // The class of key deserializer.
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getKeyDeserializer());
        // The class of value deserializer.
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getValueDeserializer());
        // A unique string that identifies the consumer group this consumer belongs to.
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfigData.getConsumerGroupId());
        // What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server.
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());
        // The URL of the schema registry.
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        // Whether to use the Specific Avro Reader.
        props.put(kafkaConsumerConfigData.getSpecificAvroReaderKey(), kafkaConsumerConfigData.getSpecificAvroReader());
        // The timeout used to detect consumer failures when using Kafka's group management facility.
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfigData.getSessionTimeoutMs());
        // The expected time between heartbeats to the consumer coordinator when using Kafka's group management facilities.
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getHeartbeatIntervalMs());
        // The maximum delay between invocations of poll() when using consumer group management.
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getMaxPollIntervalMs());
        // The maximum amount of data per-partition the server will return.
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                kafkaConsumerConfigData.getMaxPartitionFetchBytesDefault() *
                        kafkaConsumerConfigData.getMaxPartitionFetchBytesBoostFactor());
        // The maximum number of records returned in a single call to poll().
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfigData.getMaxPollRecords());
        return props;
    }

    /**
     * Provides a consumer factory for Kafka consumers.
     * @return A consumer factory for Kafka consumers.
     */
    @Bean
    public ConsumerFactory<K,V> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * Provides a Kafka listener container factory for Kafka consumers.
     * @return A Kafka listener container factory for Kafka consumers.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // Whether the container should batch listener invocations.
        factory.setBatchListener(kafkaConsumerConfigData.isBatchListener());
        // The maximum number of concurrent Listener invocations.
        factory.setConcurrency(kafkaConsumerConfigData.getConcurrencyLevel());
        // Whether the container should automatically start after initialization.
        factory.setAutoStartup(kafkaConsumerConfigData.isAutoStartup());
        // The time, in milliseconds, for the container to wait for messages in a poll.
        factory.getContainerProperties().setPollTimeout(kafkaConsumerConfigData.getPollTimeoutMs());
        return factory;
    }
}