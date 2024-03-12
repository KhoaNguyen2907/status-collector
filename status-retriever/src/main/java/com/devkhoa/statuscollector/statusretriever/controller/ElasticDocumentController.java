package com.devkhoa.statuscollector.statusretriever.controller;

import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.elasticsearch.queryclient.service.ElasticSearchQueryClient;
import com.devkhoa.statuscollector.statusretriever.model.QueryRequestModel;
import com.devkhoa.statuscollector.statusretriever.model.QueryResponseModel;
import com.devkhoa.statuscollector.statusretriever.service.ElasticQueryService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;
    Logger logger = LoggerFactory.getLogger(ElasticDocumentController.class);

    public ElasticDocumentController(ElasticSearchQueryClient<StatusIndexModel> elasticSearchQueryClient, ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @GetMapping()
    public ResponseEntity<List<QueryResponseModel>> getDocuments() {
        logger.info("Get all documents");
        List<QueryResponseModel> response =  elasticQueryService.getAllDocuments();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QueryResponseModel> getDocumentById(@PathVariable @NotNull String id) {
        logger.info("Get document by id");
        QueryResponseModel response = elasticQueryService.getDocumentById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/search-by-text")
    public ResponseEntity<List<QueryResponseModel>> getDocumentsByText(@RequestBody @Valid QueryRequestModel request) {
        logger.info("Get documents by text");
        List<QueryResponseModel> response = elasticQueryService.getDocumentsByText(request.getText());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
