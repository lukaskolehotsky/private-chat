package com.bench.privatechat.model.db.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document("message")
public class Message {

    @Id
    private UUID id;

    private UUID sender;

    private UUID receiver;

    private String message;

    private LocalDateTime createdAt;

    private Boolean displayed;

}
