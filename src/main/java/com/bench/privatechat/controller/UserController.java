package com.bench.privatechat.controller;

import static com.bench.privatechat.constants.PrivateChatAppConstant.MONGO;
import static com.bench.privatechat.constants.PrivateChatAppConstant.POSTGRE;

import com.bench.privatechat.config.PrivateChatAppProperties;
import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.UserResponse;
import com.bench.privatechat.service.UserServiceMongo;
import com.bench.privatechat.service.UserServicePostgreSQL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    public final PrivateChatAppProperties privateChatAppProperties;
    public final UserServicePostgreSQL userServicePostgreSQL;
    public final UserServiceMongo userServiceMongo;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<UserResponse>> create(
            @RequestBody UserRequest request,
            UriComponentsBuilder ucb
    ) {
        log.info("Create user : {}", request);

        Mono<UserResponse> userMono;
        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            userMono = userServiceMongo.create(request)
                    .doOnNext(user -> log.info("{} - Created user: {}", MONGO, user));
        } else {
            userMono = userServicePostgreSQL.create(request)
                    .doOnNext(user -> log.info("{} - Created user: {}", POSTGRE, user));
        }

        return userMono.map(userResponse -> {
            URI locationOfUser = ucb
                    .path("/api/users/{id}")
                    .buildAndExpand(userResponse.getId())
                    .toUri();
            return ResponseEntity
                    .created(locationOfUser)
                    .body(userResponse);
        });
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        log.info("Delete all users");

        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            return userServiceMongo.deleteAll()
                    .doOnSuccess(aVoid -> log.info("{} - Deleted all users", MONGO));
        }

        return userServicePostgreSQL.deleteAll()
                .doOnSuccess(aVoid -> log.info("{} - Deleted all users", POSTGRE));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(
            @PathVariable UUID id
    ) {
        log.info("Delete user by id: {}", id);

        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            return userServiceMongo.deleteById(id)
                    .doOnSuccess(aVoid -> log.info("{} - Deleted user by id: {}", MONGO, id));
        }

        return userServicePostgreSQL.deleteById(id)
                .doOnSuccess(aVoid -> log.info("{} - Deleted user by id: {}", POSTGRE, id));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<UserResponse>> getAll() {
        log.info("Find all users");

        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            return userServiceMongo.findAll()
                    .doOnNext(users -> log.info("{} - Found users: {}", MONGO, users));
        }

        return userServicePostgreSQL.findAll()
                .doOnNext(users -> log.info("{} - Found users: {}", POSTGRE, users));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponse> getById(
            @PathVariable UUID id
    ) {
        log.info("Find user by id");

        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            return userServiceMongo.findById(id)
                    .doOnNext(user -> log.info("{} - Found user {} by id: {}", MONGO, user, id));
        }

        return userServicePostgreSQL.findById(id)
                .doOnNext(user -> log.info("{} - Found user {} by id: {}", POSTGRE, user, id));
    }
}