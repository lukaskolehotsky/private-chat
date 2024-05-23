package com.bench.privatechat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "app")
public class PrivateChatAppProperties {

    @Value("${database}")
    private String database;
}