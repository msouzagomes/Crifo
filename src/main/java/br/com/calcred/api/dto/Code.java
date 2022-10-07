package br.com.calcred.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Code {

    CODE_0001("CRIVOAPI-0001"),
    CODE_0002("CRIVOAPI-0002"),
    CODE_0003("CRIVOAPI-0003"),
    CODE_0004("CRIVOAPI-0004");

    private final String code;
}
