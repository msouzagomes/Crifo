package br.com.calcred.api.resource.handler;

import br.com.calcred.api.integration.exception.IntegrationErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class IntegrationHandler {

    @ExceptionHandler(value = IntegrationErrorException.class)
    public ResponseEntity<String> integrationHandler(IntegrationErrorException i){
        log.debug(i.getClass() + ": " +  i.getMessage(), i);
        return new ResponseEntity<>("Exception handler tratando resposta do fallback -> " + i.getMessage(), i.getStatus());
    }
}
