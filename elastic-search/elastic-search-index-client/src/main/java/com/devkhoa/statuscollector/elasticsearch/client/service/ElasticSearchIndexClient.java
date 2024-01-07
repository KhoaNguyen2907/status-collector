package com.devkhoa.statuscollector.elasticsearch.client.service;

import com.devkhoa.statuscollector.elasticsearch.model.index.IndexModel;

import java.util.List;

public interface ElasticSearchIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
