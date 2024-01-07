package com.devkhoa.statuscollector.elasticsearch.client.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.devkhoa.statuscollector.configdata.ElasticSearchConfigData;
import com.devkhoa.statuscollector.elasticsearch.client.service.ElasticSearchIndexClient;
import com.devkhoa.statuscollector.elasticsearch.client.util.ElasticSearchIndexUtil;
import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusElasticSearchIndexClient implements ElasticSearchIndexClient<StatusIndexModel> {
    private static final Logger LOG = LoggerFactory.getLogger(StatusElasticSearchIndexClient.class);

    private final ElasticSearchConfigData elasticSearchConfigData;

    private final ElasticsearchOperations elasticsearchOperations;


    private final ElasticSearchIndexUtil<StatusIndexModel> elasticIndexUtil;

    public StatusElasticSearchIndexClient(ElasticSearchConfigData elasticSearchConfigData, ElasticsearchOperations elasticsearchOperations, ElasticSearchIndexUtil<StatusIndexModel> elasticIndexUtil) {
        this.elasticSearchConfigData = elasticSearchConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticIndexUtil = elasticIndexUtil;

    }

    @Override
    public List<String> save(List<StatusIndexModel> documents) {
        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);

        System.out.println("indexQueries: " + indexQueries);
        System.out.println("Index name: " + elasticSearchConfigData.getIndexName());


        List<IndexedObjectInformation> indexedDocumentInfo = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticSearchConfigData.getIndexName())
        );

        List<String> documentIds = indexedDocumentInfo.stream().map(IndexedObjectInformation::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type: {} and ids: {}", StatusIndexModel.class.getName(), documentIds);

        return documentIds;
    }
}
