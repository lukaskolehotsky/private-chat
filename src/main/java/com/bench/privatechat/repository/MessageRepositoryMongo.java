package com.bench.privatechat.repository;

import com.bench.privatechat.model.db.mongo.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "application.properties.database", havingValue = "mongo")
public interface MessageRepositoryMongo extends ReactiveMongoRepository<Message, UUID> {

    Flux<Message> getAllBySenderAndReceiver(UUID sender, UUID receiver);
    Flux<Message> getAllBySender(UUID sender);
    Flux<Message> getAllByReceiver(UUID receiver);

//    Mono<Boolean> existsByReceiverAndDisplayedIsFalse(UUID receiver);
    Mono<Boolean> existsBySenderAndReceiverAndDisplayedIsFalse(UUID sender, UUID receiver);

}