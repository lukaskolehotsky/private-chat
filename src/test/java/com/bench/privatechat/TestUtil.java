package com.bench.privatechat;

import com.bench.privatechat.model.request.MessageRequest;
import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.MessageResponse;
import com.bench.privatechat.model.response.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestUtil {

    public static UserResponse buildUserResponse(UUID id, String username) {
        return UserResponse.builder()
                .id(id)
                .username(username)
                .email(username + "@test.com")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserRequest buildUserRequest(String username) {
        return UserRequest.builder()
                .username(username)
                .email(username + "@test.com")
                .build();
    }

    public static MessageResponse buildMessageResponse(UUID sender, UUID receiver) {
        return MessageResponse.builder()
                .id(UUID.randomUUID())
                .sender(sender)
                .receiver(receiver)
                .message("test_message")
                .createdAt(LocalDateTime.now())
                .displayed(Boolean.FALSE)
                .build();
    }

    public static MessageRequest buildMessageRequest(UUID sender, UUID receiver) {
        return MessageRequest.builder()
                .id(UUID.randomUUID())
                .sender(sender)
                .receiver(receiver)
                .message("test_message")
                .build();
    }

}