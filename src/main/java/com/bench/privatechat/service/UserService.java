package com.bench.privatechat.service;

import com.bench.privatechat.model.db.User;
import com.bench.privatechat.model.mapper.UserMapper;
import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.UserResponse;
import com.bench.privatechat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Mono<UserResponse> create(UserRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .flatMap(user -> Mono.<UserResponse>error(new Exception("User by username already exists!")))
                .switchIfEmpty(Mono.defer(() -> userRepository.findByEmail(request.getEmail()))
                        .flatMap(user -> Mono.<UserResponse>error(new Exception("User by email already exists!")))
                        .switchIfEmpty(Mono.defer(() -> userRepository.save(userMapper.from(request)).map(userMapper::from))));
    }

    public Mono<Void> deleteAll() {
        return userRepository.deleteAll();
    }

    public Mono<Void> deleteById(UUID id) {
        return userRepository.deleteById(id);
    }

    public Mono<List<UserResponse>> findAll() {
        return userRepository.findAll()
                .map(userMapper::from)
                .collectList();
    }

}
