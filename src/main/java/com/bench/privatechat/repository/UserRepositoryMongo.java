package com.bench.privatechat.repository;

import com.bench.privatechat.model.db.mongo.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "application.properties.database", havingValue = "mongo")
public interface UserRepositoryMongo extends ReactiveMongoRepository<User, UUID> {

    Mono<User> findByUsername(String username);

    Mono<User> findByEmail(String email);
}
