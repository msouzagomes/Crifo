package br.com.calcred.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalErrorException extends ResponseStatusException {

    public InternalErrorException(final String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }

    public InternalErrorException(HttpStatus status, String reason) {
        super(status, reason);
    }
    public InternalErrorException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
