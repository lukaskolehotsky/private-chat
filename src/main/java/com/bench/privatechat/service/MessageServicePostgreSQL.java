package com.bench.privatechat.service;

import com.bench.privatechat.model.db.postgre.Message;
import com.bench.privatechat.mapper.MessageMapperPostgreSQL;
import com.bench.privatechat.model.request.MessageRequest;
import com.bench.privatechat.model.response.MessageResponse;
import com.bench.privatechat.repository.MessageRepositoryPostgreSQL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServicePostgreSQL {

    public final MessageRepositoryPostgreSQL messageRepository;
    public final MessageMapperPostgreSQL messageMapper;

    public Mono<MessageResponse> createMessage(MessageRequest request) {
        return messageRepository.save(messageMapper.from(request))
                .map(messageMapper::from);
    }

    public Mono<List<MessageResponse>> getMessages(UUID sender, UUID receiver) {
        return Flux.concat(
                        messageRepository.getAllBySenderAndReceiver(sender, receiver),
                        messageRepository.getAllBySenderAndReceiver(receiver, sender)
                )
                .sort(Comparator.comparing(Message::getCreatedAt))
                .map(messageMapper::from)
                .collectList();
    }

    public Mono<Void> deleteAll() {
        return messageRepository.deleteAll();
    }

//    public Mono<Boolean> existsByReceiverAndDisplayedIsFalse(UUID receiver) {
//        return messageRepository.existsByReceiverAndDisplayedIsFalse(receiver);
//    }

    public Mono<Boolean> existsNotDisplayedBySenderAndReceiver(UUID sender, UUID receiver) {
        return messageRepository.existsBySenderAndReceiverAndDisplayedIsFalse(sender, receiver);
    }

    public Mono<Void> readMessage(UUID id) {
        return messageRepository.findById(id)
                .flatMap(message -> {
                    if (message.getDisplayed()) {
                        log.info("Message already read.");
                        return Mono.empty();
                    }
                    message.setDisplayed(Boolean.TRUE);
                    return Mono.defer(() -> messageRepository.save(message))
                            .doOnSuccess(m -> log.info("Displayed message."));
                })
                .then();
    }

}