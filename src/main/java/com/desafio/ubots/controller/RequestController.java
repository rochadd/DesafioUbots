package com.desafio.ubots.controller;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    public String handleRequest(@RequestBody Request request) {
        String requestId = UUID.randomUUID().toString();
        switch (request.getType()) {
            case CARTAO -> redisTemplate.opsForHash().put(CARTAO.getSubject(), requestId, request);
            case EMPRESTIMO -> redisTemplate.opsForHash().put(EMPRESTIMO.getSubject(), requestId, request);
            case OUTROS -> redisTemplate.opsForHash().put(OUTROS.getSubject(), requestId, request);
            default -> throw new IllegalArgumentException("Invalid request type");
        }
        return "Request stored with ID: " + requestId;
    }
}
