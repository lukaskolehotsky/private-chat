package com.bench.privatechat.controller;

import com.bench.privatechat.model.request.MessageRequest;
import com.bench.privatechat.model.response.MessageResponse;
import com.bench.privatechat.service.MessageService;
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

    public final MessageService messageService;

    @PostMapping
    public Mono<ResponseEntity<MessageResponse>> createMessage(
            @RequestBody MessageRequest request,
            UriComponentsBuilder ucb
    ) {
        log.info("Create message : {}", request);

        return messageService.createMessage(request)
                .doOnNext(message -> log.info("Created message: {}", message))
                .map(messageResponse -> {
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

        return messageService.getMessages(sender, receiver)
                .doOnNext(messages -> log.info("Got messages: {}", messages))
                .map(ResponseEntity::ok);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        log.info("Delete all messages");

        return messageService.deleteAll()
                .doOnSuccess(aVoid -> log.info("Deleted all messages"));
    }

    @GetMapping("/exists-by-receiver-not-displayed/{receiver}")
    public Mono<ResponseEntity<Boolean>> existsByReceiverAndDisplayedIsFalse(
            @PathVariable UUID receiver
    ) {
        log.info("Exists messages by receiver: [{}]", receiver);

        return messageService.existsByReceiverAndDisplayedIsFalse(receiver)
                .doOnNext(exists -> log.info("Exists messages by receiver: {}", exists))
                .map(a -> ResponseEntity.ok(a));
    }

    @GetMapping("/exists-not-displayed-by-sender-and-receiver/{sender}/{receiver}")
    public Mono<ResponseEntity<Boolean>> existsNotDisplayedBySenderAndReceiver(
            @PathVariable UUID sender,
            @PathVariable UUID receiver
    ) {
        log.info("Exists not dislpayed messages by sender: [{}] and receiver: [{}]", sender, receiver);

        return messageService.existsNotDisplayedBySenderAndReceiver(sender, receiver)
                .doOnNext(exists -> log.info("Exists not displayed messages by sender and receiver, - {}", exists))
                .map(a -> ResponseEntity.ok(a));
    }

    @GetMapping("/read/{id}")
    public Mono<Void> readMessage(
            @PathVariable UUID id
    ) {
        log.info("Display message by id: [{}]", id);

        return messageService.readMessage(id);
    }

}
