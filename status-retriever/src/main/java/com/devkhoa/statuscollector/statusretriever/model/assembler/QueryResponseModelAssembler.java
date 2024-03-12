package com.devkhoa.statuscollector.statusretriever.model.assembler;

import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.statusretriever.controller.ElasticDocumentController;
import com.devkhoa.statuscollector.statusretriever.model.QueryResponseModel;
import com.devkhoa.statuscollector.statusretriever.utils.IndexToResponseModelConverter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class QueryResponseModelAssembler extends RepresentationModelAssemblerSupport<StatusIndexModel, QueryResponseModel> {

    public QueryResponseModelAssembler() {
        super(ElasticDocumentController.class, QueryResponseModel.class);
    }

    @Override
    public QueryResponseModel toModel(StatusIndexModel entity) {
        QueryResponseModel responseModel = IndexToResponseModelConverter.from(entity);
        responseModel.add(
                linkTo(ElasticDocumentController.class)
                        .withRel("documents"));

        responseModel.add(
                linkTo(methodOn(ElasticDocumentController.class).getDocumentById(entity.getId()))
                        .withSelfRel());
        return responseModel;
    }

    public List<QueryResponseModel> toListModel(List<StatusIndexModel> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
