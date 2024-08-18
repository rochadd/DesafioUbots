package com.desafio.ubots.controller;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.desafio.ubots.com.desafio.ubots.model.RequestType.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private final RedisTemplate<String, Request> redisTemplate;

    public RequestController(RedisTemplate<String, Request> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    public ResponseEntity<String> handleRequest(@RequestBody Request request) {
        String requestId = UUID.randomUUID().toString();
        switch (request.getType()) {
            case CARTAO -> redisTemplate.opsForHash().put(CARTAO.getSubject(), requestId, request);
            case EMPRESTIMO -> redisTemplate.opsForHash().put(EMPRESTIMO.getSubject(), requestId, request);
            case OUTROS -> redisTemplate.opsForHash().put(OUTROS.getSubject(), requestId, request);
            default -> throw new IllegalArgumentException("Invalid request type");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/requests/" + requestId)
                .body("Request stored with ID: " + requestId);
    }
}
