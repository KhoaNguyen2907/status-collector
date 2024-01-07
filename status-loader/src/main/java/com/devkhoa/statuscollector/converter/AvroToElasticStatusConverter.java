package com.devkhoa.statuscollector.converter;

import com.devkhoa.statuscollector.elasticsearch.model.index.impl.StatusIndexModel;
import com.devkhoa.statuscollector.kafka.avro.model.StatusAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class AvroToElasticStatusConverter {
    public List<StatusIndexModel> getIndexModel(List<StatusAvroModel> status) {
        return status.stream()
                .map(statusAvroModel -> StatusIndexModel
                        .builder()
                        .id(String.valueOf(statusAvroModel.getId()))
                        .userId(statusAvroModel.getUserId())
                        .text(statusAvroModel.getText())
                        .createdAt(ZonedDateTime.ofInstant(Instant.ofEpochMilli(statusAvroModel.getCreatedAt()),
                                java.time.ZoneId.systemDefault()))
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }
}
