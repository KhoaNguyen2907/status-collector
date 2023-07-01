package com.devkhoa.statuscollector.kafka.admin.exception;

public class KafkaClientException extends RuntimeException{
    String message;

    public KafkaClientException(String message ){
        this.message = message;
    }
}
