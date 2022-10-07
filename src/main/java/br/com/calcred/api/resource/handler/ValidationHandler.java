package br.com.calcred.api.resource.handler;

import br.com.calcred.api.dto.Error;
import br.com.calcred.api.dto.ErrorMessage;
import br.com.calcred.api.exception.AbstractErrorException;
import br.com.calcred.api.helper.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

import static br.com.calcred.api.integration.crivo.enumeration.ErrorCode.ERRO_INTERNO;
import static java.util.Optional.ofNullable;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ValidationHandler {

    private final MessageHelper messageHelper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(final Exception exception) {

        log.error("Erro na API", exception);

        final ErrorMessage errorInfo = ErrorMessage.builder()
                .message(messageHelper.get(ERRO_INTERNO))
                .code(ERRO_INTERNO.getCode())
                .errors(ofNullable(exception.getMessage())
                        .map(message -> Error.builder()
                                .field("reason")
                                .message(message)
                                .build())
                        .map(Collections::singletonList)
                        .orElse(null))
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AbstractErrorException.class)
    public ResponseEntity<ErrorMessage> handleException(final AbstractErrorException exception) {

        log.error("Erro na API", exception);

        final ErrorMessage errorInfo = ErrorMessage.builder()
                .message(exception.getReason())
                .code(exception.getCode()).errors(exception.getErrors())
                .errors(exception.getErrors())
                .build();

        return new ResponseEntity<>(errorInfo, exception.getStatus());
    }
}
