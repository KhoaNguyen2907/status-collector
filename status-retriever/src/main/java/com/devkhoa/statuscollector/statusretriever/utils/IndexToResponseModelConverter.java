package com.devkhoa.statuscollector.statusretriever.utils;

import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.statusretriever.model.QueryResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IndexToResponseModelConverter {
    public static QueryResponseModel from(StatusIndexModel statusIndexModel) {
        return QueryResponseModel.builder()
                .id(statusIndexModel.getId())
                .text(statusIndexModel.getText())
                .userId(statusIndexModel.getUserId())
                .createdAt(statusIndexModel.getCreatedAt())
                .build();
    }

    public static List<QueryResponseModel> from(List<StatusIndexModel> statusIndexModels) {
        return statusIndexModels.stream().map(IndexToResponseModelConverter::from).collect(Collectors.toList());
    }
}
