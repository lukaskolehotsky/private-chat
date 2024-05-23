package com.bench.privatechat.controller;

import static org.mockito.Mockito.*;
import static com.bench.privatechat.constants.PrivateChatAppConstant.MONGO;
import static com.bench.privatechat.constants.PrivateChatAppConstant.POSTGRE;

import com.bench.privatechat.TestUtil;
import com.bench.privatechat.config.PrivateChatAppProperties;
import com.bench.privatechat.model.request.UserRequest;
import com.bench.privatechat.model.response.UserResponse;
import com.bench.privatechat.service.UserServiceMongo;
import com.bench.privatechat.service.UserServicePostgreSQL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private UserServicePostgreSQL userServicePostgreSQL;

    @MockBean
    private UserServiceMongo userServiceMongo;

    @MockBean
    private PrivateChatAppProperties privateChatAppProperties;

    @Test
    void test_postgre_getAll() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        List<UserResponse> users = Arrays.asList(
                TestUtil.buildUserResponse(UUID.randomUUID(), "user1"),
                TestUtil.buildUserResponse(UUID.randomUUID(), "user2")
        );
        when(userServicePostgreSQL.findAll()).thenReturn(Mono.just(users));

        webClient.get().uri("/api/users/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .isEqualTo(users);

        verify(userServicePostgreSQL, times(1)).findAll();
        verify(userServiceMongo, times(0)).findAll();
    }

    @Test
    void test_mongo_getAll() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        List<UserResponse> users = Arrays.asList(
                TestUtil.buildUserResponse(UUID.randomUUID(), "user1"),
                TestUtil.buildUserResponse(UUID.randomUUID(), "user2")
        );
        when(userServiceMongo.findAll()).thenReturn(Mono.just(users));

        webClient.get().uri("/api/users/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .isEqualTo(users);

        verify(userServiceMongo, times(1)).findAll();
        verify(userServicePostgreSQL, times(0)).findAll();
    }

    @Test
    void test_postgre_createUser() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        UserRequest request = TestUtil.buildUserRequest("test");
        UserResponse response = TestUtil.buildUserResponse(UUID.randomUUID(), "test");
        when(userServicePostgreSQL.create(request)).thenReturn(Mono.just(response));

        webClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(response);

        verify(userServicePostgreSQL, times(1)).create(request);
        verify(userServiceMongo, times(0)).create(any(UserRequest.class));
    }

    @Test
    void test_mongo_createUser() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        UserRequest request = TestUtil.buildUserRequest("test");
        UserResponse response = TestUtil.buildUserResponse(UUID.randomUUID(), "test");
        when(userServiceMongo.create(request)).thenReturn(Mono.just(response));

        webClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(response);

        verify(userServiceMongo, times(1)).create(request);
        verify(userServicePostgreSQL, times(0)).create(any(UserRequest.class));
    }

    @Test
    void test_postgre_deleteAllUsers() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        when(userServicePostgreSQL.deleteAll()).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/users")
                .exchange()
                .expectStatus().isNoContent();

        verify(userServicePostgreSQL, times(1)).deleteAll();
        verify(userServiceMongo, times(0)).deleteAll();
    }

    @Test
    void test_mongo_deleteAllUsers() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        when(userServiceMongo.deleteAll()).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/users")
                .exchange()
                .expectStatus().isNoContent();

        verify(userServiceMongo, times(1)).deleteAll();
        verify(userServicePostgreSQL, times(0)).deleteAll();
    }

    @Test
    void test_postgre_deleteById() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        UUID id = UUID.randomUUID();
        when(userServicePostgreSQL.deleteById(id)).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/users/{id}", id)
                .exchange()
                .expectStatus().isNoContent();

        verify(userServicePostgreSQL, times(1)).deleteById(id);
        verify(userServiceMongo, times(0)).deleteById(any(UUID.class));
    }

    @Test
    void test_mongo_deleteById() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        UUID id = UUID.randomUUID();
        when(userServiceMongo.deleteById(id)).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/users/{id}", id)
                .exchange()
                .expectStatus().isNoContent();

        verify(userServiceMongo, times(1)).deleteById(id);
        verify(userServicePostgreSQL, times(0)).deleteById(any(UUID.class));
    }

    @Test
    void test_postgre_getById() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        UUID id = UUID.randomUUID();
        UserResponse userResponse = TestUtil.buildUserResponse(id, "test_user");
        when(userServicePostgreSQL.findById(id)).thenReturn(Mono.just(userResponse));

        webClient.get().uri("/api/users/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(userServicePostgreSQL, times(1)).findById(id);
        verify(userServiceMongo, times(0)).findById(any(UUID.class));
    }

    @Test
    void test_mongo_getById() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        UUID id = UUID.randomUUID();
        UserResponse userResponse = TestUtil.buildUserResponse(id, "test_user");
        when(userServiceMongo.findById(id)).thenReturn(Mono.just(userResponse));

        webClient.get().uri("/api/users/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(userServiceMongo, times(1)).findById(id);
        verify(userServicePostgreSQL, times(0)).findById(any(UUID.class));
    }
}