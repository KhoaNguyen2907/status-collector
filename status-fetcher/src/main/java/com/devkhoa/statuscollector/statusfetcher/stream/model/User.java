package com.devkhoa.statuscollector.statusfetcher.stream.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    private long id;
    private String userName;
}
