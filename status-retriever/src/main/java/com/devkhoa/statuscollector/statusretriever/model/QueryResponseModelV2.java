package com.devkhoa.statuscollector.statusretriever.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponseModelV2 {
    private long id;
    private long userId;
    private String text;
    private ZonedDateTime createdAt;
}
