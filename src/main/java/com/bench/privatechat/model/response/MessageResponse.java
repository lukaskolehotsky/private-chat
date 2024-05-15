package com.bench.privatechat.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageResponse {

    private UUID id;

    private UUID sender;

    private UUID receiver;

    private String message;

    private LocalDateTime createdAt;

    private Boolean displayed;

}
