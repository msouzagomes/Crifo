package br.com.calcred.api.exception;

import br.com.calcred.api.dto.Error;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Getter
public abstract class AbstractErrorException extends ResponseStatusException {

    private String code;
    private List<Error> errors;

    public AbstractErrorException(HttpStatus status, String reason, String code) {
        super(status, reason);
        this.code = code;
    }

    public AbstractErrorException(HttpStatus status, String reason, String code, List<Error> errors) {
        super(status, reason);
        this.code = code;
        this.errors = errors;
    }

    AbstractErrorException(HttpStatus status, String reason, String code, Throwable cause) {
        super(status, reason, cause);
        this.code = code;
    }
}
