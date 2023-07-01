package com.devkhoa.statuscollector.kafka.admin.config;

import com.devkhoa.statuscollector.configdata.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@EnableRetry
@Configuration
public class KafkaAdminConfig {
    private final KafkaConfigData data;
    public KafkaAdminConfig(KafkaConfigData data){
        this.data = data;
    }

    @Bean
    public AdminClient client(){
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                data.getBootstrapServers()));
    }
}
