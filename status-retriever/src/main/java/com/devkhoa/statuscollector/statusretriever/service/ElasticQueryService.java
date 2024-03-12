package com.devkhoa.statuscollector.statusretriever.service;

import com.devkhoa.statuscollector.statusretriever.model.QueryResponseModel;

import java.util.List;

public interface ElasticQueryService {
    List<QueryResponseModel> getAllDocuments();
    QueryResponseModel getDocumentById(String id);
    List<QueryResponseModel> getDocumentsByText(String text);
}
