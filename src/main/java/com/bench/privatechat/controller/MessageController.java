package com.bench.privatechat.controller;

import static com.bench.privatechat.constants.PrivateChatAppConstant.MONGO;
import static com.bench.privatechat.constants.PrivateChatAppConstant.POSTGRE;

import com.bench.privatechat.config.PrivateChatAppProperties;
import com.bench.privatechat.model.request.MessageRequest;
import com.bench.privatechat.model.response.MessageResponse;
import com.bench.privatechat.service.MessageServiceMongo;
import com.bench.privatechat.service.MessageServicePostgreSQL;
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
@RequestMapping("/api/messages")
public class MessageController {

    public final PrivateChatAppProperties privateChatAppProperties;
    public final MessageServicePostgreSQL messageServicePostgreSQL;
    public final MessageServiceMongo messageServiceMongo;

    @PostMapping
    public Mono<ResponseEntity<MessageResponse>> createMessage(
            @RequestBody MessageRequest request,
            UriComponentsBuilder ucb
    ) {
        log.info("Create message : {}", request);

        Mono<MessageResponse> messageMono;
        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            messageMono = messageServiceMongo.createMessage(request)
                    .doOnNext(message -> log.info("{} - Created message: {}", MONGO, message));
        } else {
            messageMono = messageServicePostgreSQL.createMessage(request)
                    .doOnNext(message -> log.info("{} - Created message: {}", POSTGRE, message));
        }

        return messageMono.map(messageResponse -> {
            URI locationOfMessage = ucb
                    .path("/api/messages/{id}")
                    .buildAndExpand(messageResponse.getId())
                    .toUri();
            return ResponseEntity
                    .created(locationOfMessage)
                    .body(messageResponse);
        });
    }

    @GetMapping("/{sender}/{receiver}")
    public Mono<ResponseEntity<List<MessageResponse>>> getMessages(
            @PathVariable UUID sender,
            @PathVariable UUID receiver
    ) {
        log.info("Get messages by sender: [{}], receiver: [{}]", sender, receiver);

        Mono<List<MessageResponse>> messagesMono;
        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            messagesMono = messageServiceMongo.getMessages(sender, receiver)
                    .doOnNext(messages -> log.info("{} - Got messages: {}", MONGO, messages));
        } else {
            messagesMono = messageServicePostgreSQL.getMessages(sender, receiver)
                    .doOnNext(messages -> log.info("{} - Got messages: {}", POSTGRE, messages));
        }

        return messagesMono.map(ResponseEntity::ok);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        log.info("Delete all messages");

        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            return messageServiceMongo.deleteAll()
                    .doOnSuccess(aVoid -> log.info("{} - Deleted all messages", MONGO));
        }

        return messageServicePostgreSQL.deleteAll()
                .doOnSuccess(aVoid -> log.info("{} - Deleted all messages", POSTGRE));
    }

//    @GetMapping("/exists-by-receiver-not-displayed/{receiver}")
//    public Mono<ResponseEntity<Boolean>> existsByReceiverAndDisplayedIsFalse(
//            @PathVariable UUID receiver
//    ) {
//        log.info("Exists messages by receiver: [{}]", receiver);
//
//        Mono<Boolean> existsMono;
//        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
//            existsMono = messageServiceMongo.existsByReceiverAndDisplayedIsFalse(receiver)
//                    .doOnNext(exists -> log.info("{} - Exists messages by receiver: {}", MONGO, exists));
//        } else {
//            existsMono = messageServicePostgreSQL.existsByReceiverAndDisplayedIsFalse(receiver)
//                    .doOnNext(exists -> log.info("{} - Exists messages by receiver: {}", POSTGRE, exists));
//        }
//
//        return existsMono.map(ResponseEntity::ok);
//    }

    @GetMapping("/exists-not-displayed-by-sender-and-receiver/{sender}/{receiver}")
    public Mono<ResponseEntity<Boolean>> existsNotDisplayedBySenderAndReceiver(
            @PathVariable UUID sender,
            @PathVariable UUID receiver
    ) {
        log.info("Exists not displayed messages by sender: [{}] and receiver: [{}]", sender, receiver);

        Mono<Boolean> existsMono;
        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            existsMono = messageServiceMongo.existsNotDisplayedBySenderAndReceiver(sender, receiver)
                    .doOnNext(exists -> log.info("{} - Exists not displayed messages by sender and receiver, - {}", MONGO, exists));
        } else {
            existsMono = messageServicePostgreSQL.existsNotDisplayedBySenderAndReceiver(sender, receiver)
                    .doOnNext(exists -> log.info("{} - Exists not displayed messages by sender and receiver, - {}", POSTGRE, exists));
        }

        return existsMono.map(ResponseEntity::ok);
    }

    @GetMapping("/read/{id}")
    public Mono<Void> readMessage(
            @PathVariable UUID id
    ) {
        log.info("Display message by id: [{}]", id);

        if (MONGO.equals(privateChatAppProperties.getDatabase())) {
            return messageServiceMongo.readMessage(id);
        }

        return messageServicePostgreSQL.readMessage(id);
    }

}