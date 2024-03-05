package com.devkhoa.statuscollector.elasticsearch.queryclient.exception;

public class ElasticQueryException extends RuntimeException{
    public ElasticQueryException(String message) {
        super(message);
    }
}
