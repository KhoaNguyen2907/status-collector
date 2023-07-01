package com.devkhoa.statuscollector.statusfetcher.converter;

import com.devkhoa.statuscollector.kafka.avro.model.StatusAvroModel;
import com.devkhoa.statuscollector.statusfetcher.stream.model.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusToAvroConverter {
    public StatusAvroModel getStatusAvroModel(Status status) {
        return StatusAvroModel.newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(status.getCreatedAt().getTime())
                .build();
    }
}
