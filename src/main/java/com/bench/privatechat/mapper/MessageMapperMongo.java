package com.bench.privatechat.mapper;

import com.bench.privatechat.model.db.mongo.Message;
import com.bench.privatechat.model.request.MessageRequest;
import com.bench.privatechat.model.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "application.properties.database", havingValue = "mongo")
public class MessageMapperMongo {

    private final Clock clock;

    public Message from(MessageRequest request) {
        return Message.builder()
                .id(UUID.randomUUID())
                .sender(request.getSender())
                .receiver(request.getReceiver())
                .message(request.getMessage())
                .createdAt(LocalDateTime.now(clock))
                .displayed(Boolean.FALSE)
                .build();
    }

    public MessageResponse from(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .displayed(message.getDisplayed())
                .build();
    }

}