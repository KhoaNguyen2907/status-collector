package com.devkhoa.statuscollector.statusretriever.controller;

import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.elasticsearch.queryclient.service.ElasticSearchQueryClient;
import com.devkhoa.statuscollector.statusretriever.model.QueryRequestModel;
import com.devkhoa.statuscollector.statusretriever.model.QueryResponseModel;
import com.devkhoa.statuscollector.statusretriever.model.QueryResponseModelV2;
import com.devkhoa.statuscollector.statusretriever.service.ElasticQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(value = "/documents" , produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {
    private final ElasticQueryService elasticQueryService;
    Logger logger = LoggerFactory.getLogger(ElasticDocumentController.class);

    public ElasticDocumentController(ElasticSearchQueryClient<StatusIndexModel> elasticSearchQueryClient, ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @Operation(summary = "Get all documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all documents", content = {
                    @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema(implementation = QueryResponseModel.class))
            }),
            @ApiResponse(responseCode = "404", description = "No documents found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping()
    public ResponseEntity<List<QueryResponseModel>> getDocuments() {
        logger.info("Get all documents");
        List<QueryResponseModel> response =  elasticQueryService.getAllDocuments();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get documents by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all documents", content = {
                    @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema(implementation = QueryResponseModel.class))
            }),
            @ApiResponse(responseCode = "404", description = "No documents found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QueryResponseModel> getDocumentById(@PathVariable @NotNull String id) {
        logger.info("Get document by id: {}", id);
        QueryResponseModel response = elasticQueryService.getDocumentById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Get documents by id v2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all documents",
                    content = {
                    @Content(mediaType = "application/vnd.api.v2+json", schema = @Schema(implementation = QueryResponseModelV2.class))
            }),
            @ApiResponse(responseCode = "404", description = "No documents found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public ResponseEntity<QueryResponseModelV2> getDocumentByIdV2(@PathVariable @NotNull String id) {
        logger.info("Get document by id: {}", id);
        QueryResponseModel response = elasticQueryService.getDocumentById(id);
        QueryResponseModelV2 responseV2 = getResponseModelV2(response);

        return new ResponseEntity<>(responseV2, HttpStatus.OK);
    }

    @Operation(summary = "Get documents by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all documents", content = {
                    @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema(implementation = QueryResponseModel.class))
            }),
            @ApiResponse(responseCode = "404", description = "No documents found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/search-by-text")
    public ResponseEntity<List<QueryResponseModel>> getDocumentsByText(@RequestBody @Valid QueryRequestModel request) {
        logger.info("Get documents by text: {} ", request.getText());
        List<QueryResponseModel> response = elasticQueryService.getDocumentsByText(request.getText());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private QueryResponseModelV2 getResponseModelV2(QueryResponseModel response) {
        return QueryResponseModelV2.builder()
                .id(Long.parseLong(response.getId()))
                .userId(response.getUserId())
                .text(response.getText())
                .createdAt(response.getCreatedAt())
                .build();
    }
}
