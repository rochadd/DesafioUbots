package com.desafio.ubots.com.desafio.ubots.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RequestType {

    CARTAO("Cartao"), EMPRESTIMO("Emprestimo"), OUTROS("Outros");

    private final String subject;

    RequestType(String subject) {
        this.subject = subject;
    }

    public static RequestType fromValue(String type) {
        return Arrays
                .stream(RequestType.values())
                .filter(request -> request.getSubject().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
