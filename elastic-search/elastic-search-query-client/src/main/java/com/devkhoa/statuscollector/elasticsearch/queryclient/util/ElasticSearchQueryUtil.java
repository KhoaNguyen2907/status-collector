package com.devkhoa.statuscollector.elasticsearch.queryclient.util;

import com.devkhoa.statuscollector.elasticsearch.model.index.IndexModel;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * ElasticSearchQueryUtil is a utility class for creating Elasticsearch queries.
 * It provides methods for creating queries by ID, by field text, and for all documents.
 */
@Component
public class ElasticSearchQueryUtil <T extends IndexModel> {
    /**
     * Creates a query for a document by its ID.
     * @param id The ID of the document.
     * @return The created query.
     */
    public Query getSearchQueryById(String id){
        return new NativeSearchQueryBuilder().withIds(id).build();
    }

    /**
     * Creates a query for documents that contain a given text in a given field.
     * @param field The field to search in.
     * @param text The text to search for.
     * @return The created query.
     */
    public Query getSearchQueryByFieldText(String field, String text){
        return new NativeSearchQueryBuilder().withQuery(new BoolQueryBuilder().must(QueryBuilders.matchQuery(field,text))).build();
    }

    /**
     * Creates a query for all documents.
     * @return The created query.
     */
    public Query getSearchQueryForAll(){
        return new NativeSearchQueryBuilder().withQuery(new BoolQueryBuilder().must(QueryBuilders.matchAllQuery())).build();
    }
}