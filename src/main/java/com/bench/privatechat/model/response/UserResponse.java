package com.bench.privatechat.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;

    private String username;

    private String email;

    private LocalDateTime createdAt;

}
