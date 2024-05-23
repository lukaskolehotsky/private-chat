package com.bench.privatechat.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(PrivateChatAppProperties.class)
public class PrivateChatAppConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}