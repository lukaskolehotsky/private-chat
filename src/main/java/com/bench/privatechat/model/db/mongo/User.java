package com.bench.privatechat.model.db.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document("user_tbl")
public class User {

    @Id
    private UUID id;

    private String username;

    private String email;

    private LocalDateTime createdAt;

}