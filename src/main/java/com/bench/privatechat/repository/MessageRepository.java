package com.bench.privatechat.repository;

import com.bench.privatechat.model.db.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, UUID> {

    Flux<Message> getAllBySenderAndReceiver(UUID sender, UUID receiver);
    Flux<Message> getAllBySender(UUID sender);
    Flux<Message> getAllByReceiver(UUID receiver);

    Mono<Boolean> existsByReceiverAndDisplayedIsFalse(UUID receiver);
    Mono<Boolean> existsBySenderAndReceiverAndDisplayedIsFalse(UUID sender, UUID receiver);
}
