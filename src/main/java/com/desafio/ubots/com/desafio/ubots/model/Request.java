package com.desafio.ubots.com.desafio.ubots.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Request implements Serializable {

    private RequestType type;
    private String content;
}
