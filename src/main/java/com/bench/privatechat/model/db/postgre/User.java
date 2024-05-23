package com.bench.privatechat.model.db.postgre;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "user_tbl")
@Builder
public class User {

    @Id
    private UUID id;

    private String username;

    private String email;

    private LocalDateTime createdAt;
}
