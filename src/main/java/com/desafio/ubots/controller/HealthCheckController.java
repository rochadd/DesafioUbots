package com.desafio.ubots.controller;

import com.desafio.ubots.com.desafio.ubots.model.RequestType;
import com.desafio.ubots.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests/health")
public class HealthCheckController {

    @Autowired
    private final ConsumerService consumerService;

    public HealthCheckController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping
    public ResponseEntity<Integer> queueInfo(@RequestParam String requestType) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header("Location", "/api/requests/health/" + requestType)
                .body(consumerService.queueInfo(RequestType.fromValue(requestType)));
    }
}
