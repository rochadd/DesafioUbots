package com.desafio.ubots.com.desafio.ubots.model;

import lombok.Getter;

@Getter
public enum RequestType {

    CARTAO("Cartao"), EMPRESTIMO("Emprestimo"), OUTROS("Outros");

    private final String subject;

    RequestType(String subject) {
        this.subject = subject;
    }
}
