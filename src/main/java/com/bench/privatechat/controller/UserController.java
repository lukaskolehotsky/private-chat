package com.bench.privatechat.controller;

import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.UserResponse;
import com.bench.privatechat.service.UserService;
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

    public final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(
            @RequestBody UserRequest request,
            UriComponentsBuilder ucb
    ) {
        log.info("Create user : {}", request);

        return userService.create(request)
                .doOnNext(user -> log.info("Created user: {}", user))
                .map(userResponse ->
                        {
                            URI locationOfUser = ucb
                                    .path("/api/users/{id}")
                                    .buildAndExpand(userResponse.getId())
                                    .toUri();
                            return ResponseEntity
                                    .created(locationOfUser)
                                    .body(userResponse);
                        }
                );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        log.info("Delete all users");

        return userService.deleteAll()
                .doOnSuccess(aVoid -> log.info("Deleted all users"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(
            @PathVariable UUID id
    ) {
        log.info("Delete user by id: {}", id);

        return userService.deleteById(id)
                .doOnSuccess(aVoid -> log.info("Deleted user by id: {}", id));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<UserResponse>> getAll() {
        log.info("Find all users");

        return userService.findAll()
                .doOnNext(users -> log.info("Found users: {}", users));
    }

}