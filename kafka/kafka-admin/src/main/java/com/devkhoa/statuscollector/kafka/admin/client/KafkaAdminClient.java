package com.devkhoa.statuscollector.kafka.admin.client;

import com.devkhoa.statuscollector.kafka.admin.exception.KafkaClientException;
import com.devkhoa.statuscollector.configdata.KafkaConfigData;
import com.devkhoa.statuscollector.configdata.RetryConfigData;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaAdminClient {
    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(KafkaAdminClient.class);

    public KafkaAdminClient(KafkaConfigData kafkaConfigData, RetryConfigData retryConfigData, AdminClient adminClient, RetryTemplate retryTemplate, WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }

    public void createTopics(){
        try {
            CreateTopicsResult createTopicsResult = retryTemplate.execute(this::doCreateTopics);
            logger.info("Create topic result: {}", createTopicsResult.values().values());
            boolean isAllTopicsCreated = retryTemplate.execute(this::checkTopicsCreated);
            logger.info("Is all topic created: {}", isAllTopicsCreated);
        } catch (Exception e) {
            throw new KafkaClientException("Reached max number of retry");
        }
    }

    public void checkSchemaRegistry() {
        retryTemplate.execute(retryContext -> {
            HttpStatus status = getSchemaRegistryStatus();
            if (!status.is2xxSuccessful()) {
                throw new KafkaClientException("Cannot connect to Schema Registry");
            }
            return true;
        });
    }

    private HttpStatus getSchemaRegistryStatus() {
        try {
            return webClient
                    .method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchange()
                    .map(r -> r.statusCode())
//                    .exchangeToMono(response -> Mono.just(response.statusCode()))
                    .block();
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
        List<NewTopic> topics = topicNames.stream()
                .map(topic -> new NewTopic(topic, kafkaConfigData.getNumOfPartitions(), kafkaConfigData.getReplicationFactor()))
                .collect(Collectors.toList());
        return adminClient.createTopics(topics);
    }


    public boolean checkTopicsCreated(RetryContext retryContext) throws ExecutionException, InterruptedException {
        List<TopicListing> topics = getTopic();
        logger.info("Created topics: {}", topics);
        for (String topicNeedToCreate : kafkaConfigData.getTopicNamesToCreate()) {
            if (!isTopicCreated(topicNeedToCreate,topics)) {
                throw new KafkaClientException("Topic still not be created: " + topicNeedToCreate);
            }
        }
        return true;
    }

    private boolean isTopicCreated(String topicNeedToCreate, List<TopicListing> topics) {
       return topics.stream().anyMatch(topicListing -> topicListing.name().equalsIgnoreCase(topicNeedToCreate));
    }

    private List<TopicListing> getTopic() throws ExecutionException, InterruptedException {
        return new ArrayList<>(adminClient.listTopics().listings().get());
    }
}
