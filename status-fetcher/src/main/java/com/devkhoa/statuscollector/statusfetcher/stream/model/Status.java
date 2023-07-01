package com.devkhoa.statuscollector.statusfetcher.stream.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;


@Data
public class Status {
    private long id;
    private User user;
    private String text;
    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd yyyy HH:mm:ss")
    private Date createdAt;
}
