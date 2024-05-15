package com.bench.privatechat.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MessageRequest {

    private UUID id;

    private UUID sender;

    private UUID receiver;

    private String message;

}
