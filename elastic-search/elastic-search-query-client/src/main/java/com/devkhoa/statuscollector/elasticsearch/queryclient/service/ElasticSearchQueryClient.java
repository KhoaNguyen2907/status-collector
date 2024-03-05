package com.devkhoa.statuscollector.elasticsearch.queryclient.service;

import com.devkhoa.statuscollector.elasticsearch.model.index.IndexModel;

import java.util.List;

public interface ElasticSearchQueryClient<T extends IndexModel> {
    T getIndexModelById(String id);
    List<T> getIndexModelsByText(String text);
    List<T> getAllIndexModels();
}
