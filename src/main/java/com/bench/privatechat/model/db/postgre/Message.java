package com.bench.privatechat.model.db.postgre;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table
@Builder
public class Message {

    @Id
    private UUID id;

    private UUID sender;

    private UUID receiver;

    private String message;

    private LocalDateTime createdAt;

    private Boolean displayed;

}
