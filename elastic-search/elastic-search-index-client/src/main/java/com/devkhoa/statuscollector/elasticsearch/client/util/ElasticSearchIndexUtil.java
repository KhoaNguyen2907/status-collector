package com.devkhoa.statuscollector.elasticsearch.client.util;

import com.devkhoa.statuscollector.elasticsearch.model.index.IndexModel;
import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticSearchIndexUtil<T extends IndexModel>  {

    public List<IndexQuery> getIndexQueries(List<T> documents) {
        return documents.stream()
                .map(document -> new IndexQueryBuilder()
                        .withId(document.getId())
                        .withObject(document)
                        .build()
                ).collect(Collectors.toList());
    }
}
