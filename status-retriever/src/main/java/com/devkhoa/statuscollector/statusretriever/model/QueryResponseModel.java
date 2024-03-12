package com.devkhoa.statuscollector.statusretriever.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponseModel extends RepresentationModel<QueryResponseModel> {
    private String id;
    private long userId;
    private String text;
    private ZonedDateTime createdAt;
}
