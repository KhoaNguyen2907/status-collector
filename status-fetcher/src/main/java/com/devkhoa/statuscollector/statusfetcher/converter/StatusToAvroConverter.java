package com.devkhoa.statuscollector.statusfetcher.converter;

import com.devkhoa.statuscollector.kafka.avro.model.StatusAvroModel;
import com.devkhoa.statuscollector.statusfetcher.stream.model.Status;
import org.springframework.stereotype.Component;

/**
 * This component is responsible for converting Status objects to StatusAvroModel objects.
 */
@Component
public class StatusToAvroConverter {

    /**
     * Converts a Status object to a StatusAvroModel object.
     *
     * @param status The Status object to be converted.
     * @return The converted StatusAvroModel object.
     */
    public StatusAvroModel getStatusAvroModel(Status status) {
        return StatusAvroModel.newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(status.getCreatedAt().getTime())
                .build();
    }
}