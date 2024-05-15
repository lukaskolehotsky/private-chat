package com.bench.privatechat.model.mapper;

import com.bench.privatechat.model.db.User;
import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final Clock clock;

    public User from(UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .createdAt(LocalDateTime.now(clock))
                .build();
    }

    public UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
