package com.devkhoa.statuscollector.statusretriever.service.impl;

import com.devkhoa.statuscollector.configdata.ElasticSearchQueryConfigData;
import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.elasticsearch.queryclient.service.ElasticSearchQueryClient;
import com.devkhoa.statuscollector.statusretriever.model.QueryResponseModel;
import com.devkhoa.statuscollector.statusretriever.model.assembler.QueryResponseModelAssembler;
import com.devkhoa.statuscollector.statusretriever.service.ElasticQueryService;
import com.devkhoa.statuscollector.statusretriever.utils.IndexToResponseModelConverter;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusElasticQueryService implements ElasticQueryService {
    private final ElasticSearchQueryClient<StatusIndexModel> queryClient;
    private final QueryResponseModelAssembler queryResponseModelAssembler;
    public StatusElasticQueryService(ElasticSearchQueryConfigData elasticSearchQueryConfigData, RestHighLevelClient restHighLevelClient, ElasticSearchQueryClient<StatusIndexModel> queryClient, QueryResponseModelAssembler queryResponseModelAssembler) {
        this.queryClient = queryClient;
        this.queryResponseModelAssembler = queryResponseModelAssembler;
    }

    @Override
    public List<QueryResponseModel> getAllDocuments() {
        List<StatusIndexModel> allDocuments =  queryClient.getAllIndexModels();

        return queryResponseModelAssembler.toListModel(allDocuments);
    }

    @Override
    public QueryResponseModel getDocumentById(String id) {
        StatusIndexModel document = queryClient.getIndexModelById(id);
        return queryResponseModelAssembler.toModel(document);
    }

    @Override
    public List<QueryResponseModel> getDocumentsByText(String text) {
        List<StatusIndexModel> documents = queryClient.getIndexModelsByText(text);
        return queryResponseModelAssembler.toListModel(documents);
    }
}
