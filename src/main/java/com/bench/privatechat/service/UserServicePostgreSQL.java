package com.bench.privatechat.service;

import com.bench.privatechat.mapper.UserMapperPostgreSQL;
import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.UserResponse;
import com.bench.privatechat.repository.MessageRepositoryPostgreSQL;
import com.bench.privatechat.repository.UserRepositoryPostgreSQL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServicePostgreSQL {

    private final UserRepositoryPostgreSQL userRepositoryPostgreSQL;
    private final MessageRepositoryPostgreSQL messageRepository;
    private final UserMapperPostgreSQL userMapperPostgreSQL;

    public Mono<UserResponse> create(UserRequest request) {
        return userRepositoryPostgreSQL.findByUsername(request.getUsername())
                .flatMap(user -> Mono.<UserResponse>error(new Exception("User by username already exists!")))
                .switchIfEmpty(Mono.defer(() -> userRepositoryPostgreSQL.findByEmail(request.getEmail()))
                        .flatMap(user -> Mono.<UserResponse>error(new Exception("User by email already exists!")))
                        .switchIfEmpty(Mono.defer(() -> userRepositoryPostgreSQL.save(userMapperPostgreSQL.from(request)).map(userMapperPostgreSQL::from))));
    }

    public Mono<Void> deleteAll() {
        return messageRepository.deleteAll()
                .then(userRepositoryPostgreSQL.deleteAll());
    }

    public Mono<Void> deleteById(UUID id) {
        return Mono.defer(() ->
                Flux.concat(
                                messageRepository.getAllBySender(id),
                                messageRepository.getAllByReceiver(id)
                        )
                        .flatMap(messageRepository::delete)
                        .then(userRepositoryPostgreSQL.deleteById(id))
        );
    }

    public Mono<List<UserResponse>> findAll() {
        return userRepositoryPostgreSQL.findAll()
                .map(userMapperPostgreSQL::from)
                .collectList();
    }

    public Mono<UserResponse> findById(UUID id) {
        return userRepositoryPostgreSQL.findById(id)
                .map(userMapperPostgreSQL::from);
    }

}