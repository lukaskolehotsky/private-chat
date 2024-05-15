package com.bench.privatechat.model.mapper;

import com.bench.privatechat.model.db.Message;
import com.bench.privatechat.model.request.MessageRequest;
import com.bench.privatechat.model.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final Clock clock;

    public Message from(MessageRequest request) {
        return Message.builder()
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
