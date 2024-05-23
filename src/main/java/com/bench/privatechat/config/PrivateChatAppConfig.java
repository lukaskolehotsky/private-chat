package com.bench.privatechat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PrivateChatAppConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}