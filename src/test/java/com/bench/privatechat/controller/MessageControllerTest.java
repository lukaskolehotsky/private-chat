package com.bench.privatechat.controller;

import static com.bench.privatechat.constants.PrivateChatAppConstant.MONGO;
import static com.bench.privatechat.constants.PrivateChatAppConstant.POSTGRE;

import com.bench.privatechat.TestUtil;
import com.bench.privatechat.config.PrivateChatAppProperties;
import com.bench.privatechat.model.request.MessageRequest;
import com.bench.privatechat.model.response.MessageResponse;
import com.bench.privatechat.service.MessageServiceMongo;
import com.bench.privatechat.service.MessageServicePostgreSQL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@WebFluxTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private MessageServicePostgreSQL messageServicePostgreSQL;

    @MockBean
    private MessageServiceMongo messageServiceMongo;

    @MockBean
    private PrivateChatAppProperties privateChatAppProperties;

    @Test
    void test_postgre_createMessage() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();
        MessageRequest request = TestUtil.buildMessageRequest(sender, receiver);
        MessageResponse response = TestUtil.buildMessageResponse(sender, receiver);
        when(messageServicePostgreSQL.createMessage(request)).thenReturn(Mono.just(response));

        webClient.post()
                .uri("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MessageResponse.class)
                .isEqualTo(response);

        verify(messageServicePostgreSQL, times(1)).createMessage(request);
        verify(messageServiceMongo, times(0)).createMessage(any(MessageRequest.class));
    }

    @Test
    void test_mongo_createMessage() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();
        MessageRequest request = TestUtil.buildMessageRequest(sender, receiver);
        MessageResponse response = TestUtil.buildMessageResponse(sender, receiver);
        when(messageServiceMongo.createMessage(request)).thenReturn(Mono.just(response));

        webClient.post()
                .uri("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MessageResponse.class)
                .isEqualTo(response);

        verify(messageServiceMongo, times(1)).createMessage(request);
        verify(messageServicePostgreSQL, times(0)).createMessage(any(MessageRequest.class));
    }

    @Test
    void test_postgre_getMessages() {
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        List<MessageResponse> messageResponses = Collections.singletonList(
                TestUtil.buildMessageResponse(sender, receiver)
        );
        when(messageServicePostgreSQL.getMessages(sender, receiver)).thenReturn(Mono.just(messageResponses));

        webClient.get().uri("/api/messages/{sender}/{receiver}", sender, receiver)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageResponse.class)
                .isEqualTo(messageResponses);

        verify(messageServicePostgreSQL, times(1)).getMessages(sender, receiver);
        verify(messageServiceMongo, times(0)).getMessages(any(UUID.class), any(UUID.class));
    }

    @Test
    void test_mongo_getMessages() {
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        List<MessageResponse> messageResponses = Collections.singletonList(
                TestUtil.buildMessageResponse(sender, receiver)
        );
        when(messageServiceMongo.getMessages(sender, receiver)).thenReturn(Mono.just(messageResponses));

        webClient.get().uri("/api/messages/{sender}/{receiver}", sender, receiver)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageResponse.class)
                .isEqualTo(messageResponses);

        verify(messageServiceMongo, times(1)).getMessages(sender, receiver);
        verify(messageServicePostgreSQL, times(0)).getMessages(any(UUID.class), any(UUID.class));
    }

    @Test
    void test_postgre_deleteAll() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        when(messageServicePostgreSQL.deleteAll()).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/messages")
                .exchange()
                .expectStatus().isNoContent();

        verify(messageServicePostgreSQL, times(1)).deleteAll();
        verify(messageServiceMongo, times(0)).deleteAll();
    }

    @Test
    void test_mongo_deleteAll() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        when(messageServiceMongo.deleteAll()).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/messages")
                .exchange()
                .expectStatus().isNoContent();

        verify(messageServiceMongo, times(1)).deleteAll();
        verify(messageServicePostgreSQL, times(0)).deleteAll();
    }

    @Test
    void test_postgre_existsNotDisplayedBySenderAndReceiver() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();
        Boolean response = Boolean.TRUE;
        when(messageServicePostgreSQL.existsNotDisplayedBySenderAndReceiver(sender, receiver)).thenReturn(Mono.just(response));

        webClient.get().uri("/api/messages/exists-not-displayed-by-sender-and-receiver/{sender}/{receiver}", sender, receiver)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(response);

        verify(messageServicePostgreSQL, times(1)).existsNotDisplayedBySenderAndReceiver(sender, receiver);
        verify(messageServiceMongo, times(0)).existsNotDisplayedBySenderAndReceiver(any(UUID.class), any(UUID.class));
    }

    @Test
    void test_mongo_existsNotDisplayedBySenderAndReceiver() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();
        Boolean response = Boolean.TRUE;
        when(messageServiceMongo.existsNotDisplayedBySenderAndReceiver(sender, receiver)).thenReturn(Mono.just(response));

        webClient.get().uri("/api/messages/exists-not-displayed-by-sender-and-receiver/{sender}/{receiver}", sender, receiver)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(response);

        verify(messageServiceMongo, times(1)).existsNotDisplayedBySenderAndReceiver(sender, receiver);
        verify(messageServicePostgreSQL, times(0)).existsNotDisplayedBySenderAndReceiver(any(UUID.class), any(UUID.class));
    }

    @Test
    void test_postgre_readMessage() {
        when(privateChatAppProperties.getDatabase()).thenReturn(POSTGRE);
        UUID messageId = UUID.randomUUID();
        when(messageServicePostgreSQL.readMessage(messageId)).thenReturn(Mono.empty());

        webClient.get().uri("/api/messages/read/{id}", messageId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        verify(messageServicePostgreSQL, times(1)).readMessage(messageId);
        verify(messageServiceMongo, times(0)).readMessage(any(UUID.class));
    }

    @Test
    void test_mongo_readMessage() {
        when(privateChatAppProperties.getDatabase()).thenReturn(MONGO);
        UUID messageId = UUID.randomUUID();
        when(messageServiceMongo.readMessage(messageId)).thenReturn(Mono.empty());

        webClient.get().uri("/api/messages/read/{id}", messageId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        verify(messageServiceMongo, times(1)).readMessage(messageId);
        verify(messageServicePostgreSQL, times(0)).readMessage(any(UUID.class));
    }
}