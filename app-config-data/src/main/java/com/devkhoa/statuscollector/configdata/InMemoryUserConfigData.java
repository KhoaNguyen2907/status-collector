package com.devkhoa.statuscollector.configdata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "user-config")
@Data
public class InMemoryUserConfigData {
    private String username;
    private String password;
    private List<String> roles;
}
