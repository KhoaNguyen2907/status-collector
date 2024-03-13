package com.devkhoa.statuscollector.elasticsearch.queryclient.service.impl;

import com.devkhoa.statuscollector.configdata.ElasticSearchConfigData;
import com.devkhoa.statuscollector.configdata.ElasticSearchQueryConfigData;
import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.elasticsearch.queryclient.exception.ElasticQueryException;
import com.devkhoa.statuscollector.elasticsearch.queryclient.service.ElasticSearchQueryClient;
import com.devkhoa.statuscollector.elasticsearch.queryclient.util.ElasticSearchQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StatusElasticSearchQueryClient is a service that handles Elasticsearch queries for StatusIndexModel.
 */
@Service
public class StatusElasticSearchQueryClient implements ElasticSearchQueryClient<StatusIndexModel> {
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticSearchQueryUtil<StatusIndexModel> util;
    private final ElasticSearchQueryConfigData queryConfigData;

    private final Logger LOG = LoggerFactory.getLogger(StatusElasticSearchQueryClient.class);

    public StatusElasticSearchQueryClient(ElasticsearchOperations elasticsearchOperations, ElasticSearchQueryUtil<StatusIndexModel> util, ElasticSearchQueryConfigData queryConfigData) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.util = util;
        this.queryConfigData = queryConfigData;
    }

    /**
     * Gets a StatusIndexModel by its ID.
     * @param id The ID of the StatusIndexModel.
     * @return The StatusIndexModel with the given ID.
     */
    @Override
    public StatusIndexModel getIndexModelById(String id) {
        Query query = util.getSearchQueryById(id);
        SearchHit<StatusIndexModel> searchResult = elasticsearchOperations.searchOne(query, StatusIndexModel.class, IndexCoordinates.of(queryConfigData.getIndexName()));
        if (searchResult == null) {
            LOG.error("Nothing status found with id {}",id);
            throw new ElasticQueryException("Not found any result with id " + id);
        }
        return searchResult.getContent();
    }

    /**
     * Gets a list of StatusIndexModels that contain a given text.
     * @param text The text to search for.
     * @return A list of StatusIndexModels that contain the given text.
     */
    @Override
    public List<StatusIndexModel> getIndexModelsByText(String text) {
        Query query = util.getSearchQueryByFieldText(queryConfigData.getTextField(), text);
        return search(query, "Number result with text {}: {}", text);
    }

    /**
     * Gets a list of all StatusIndexModels.
     * @return A list of all StatusIndexModels.
     */
    @Override
    public List<StatusIndexModel> getAllIndexModels() {
        Query query = util.getSearchQueryForAll();
        return search(query, "Number result: {} {}" ,"" );
    }

    /**
     * Executes a search query and returns the results.
     * @param query The query to execute.
     * @param logMessage The message to log.
     * @param logParams The parameters for the log message.
     * @return A list of StatusIndexModels that match the query.
     */
    private List<StatusIndexModel> search(Query query, String logMessage, String... logParams) {
        LOG.info("Executing search query index: {} ", queryConfigData.getIndexName());
        //host
        SearchHits<StatusIndexModel> searchResult = elasticsearchOperations.search(query, StatusIndexModel.class, IndexCoordinates.of(queryConfigData.getIndexName()));

        LOG.info(logMessage, logParams, searchResult.getTotalHits());
        return searchResult.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}