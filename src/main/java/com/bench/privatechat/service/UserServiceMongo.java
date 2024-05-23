package com.bench.privatechat.service;

import com.bench.privatechat.mapper.UserMapperMongo;
import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.UserResponse;
import com.bench.privatechat.repository.MessageRepositoryMongo;
import com.bench.privatechat.repository.UserRepositoryMongo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.database", havingValue = "mongo")
public class UserServiceMongo {

    private final UserRepositoryMongo userRepositoryMongo;
    private final UserMapperMongo userMapperMongo;
    private final MessageRepositoryMongo messageRepositoryMongo;

    public Mono<UserResponse> create(UserRequest request) {
        return userRepositoryMongo.findByUsername(request.getUsername())
                .flatMap(user -> Mono.<UserResponse>error(new Exception("User by username already exists!")))
                .switchIfEmpty(Mono.defer(() -> userRepositoryMongo.findByEmail(request.getEmail()))
                        .flatMap(user -> Mono.<UserResponse>error(new Exception("User by email already exists!")))
                        .switchIfEmpty(Mono.defer(() -> userRepositoryMongo.save(userMapperMongo.from(request)).map(userMapperMongo::from))));
    }

    public Mono<Void> deleteAll() {
        return messageRepositoryMongo.deleteAll()
                .then(userRepositoryMongo.deleteAll());
    }

    public Mono<Void> deleteById(UUID id) {
        return Mono.defer(() ->
                Flux.concat(
                                messageRepositoryMongo.getAllBySender(id),
                                messageRepositoryMongo.getAllByReceiver(id)
                        )
                        .flatMap(messageRepositoryMongo::delete)
                        .then(userRepositoryMongo.deleteById(id))
        );
    }

    public Mono<List<UserResponse>> findAll() {
        return userRepositoryMongo.findAll()
                .map(userMapperMongo::from)
                .collectList();
    }

}