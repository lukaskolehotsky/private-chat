package com.bench.privatechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.properties")
@Data
public class PrivateChatAppProperties {

    private String database;
}