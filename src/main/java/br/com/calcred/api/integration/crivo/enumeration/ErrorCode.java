package br.com.calcred.api.integration.crivo.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static br.com.calcred.api.dto.Code.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ERRO_INTERNO("erro.interno", CODE_0001.getCode(), INTERNAL_SERVER_ERROR),
    ERRO_SERVICO_CRIVO_INDISPONIVEL("erro.servico.crivo.indisponivel", CODE_0002.getCode(), INTERNAL_SERVER_ERROR),
    ERRO_CONSULTA_LIMITE_NEGADO("erro.consulta.limite.negado", CODE_0003.getCode(), NOT_ACCEPTABLE),
    ERRO_PARSER_XML_JUSTIFICATIVAS("erro.parser.xml.justificativas", CODE_0004.getCode(), INTERNAL_SERVER_ERROR);

    private final String messageKey;
    private final String code;
    private final HttpStatus httpStatus;
}
