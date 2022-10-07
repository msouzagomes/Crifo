package br.com.calcred.api.integration.exception;

import br.com.calcred.api.dto.Error;
import br.com.calcred.api.exception.AbstractErrorException;
import br.com.calcred.api.integration.crivo.enumeration.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class IntegrationErrorException extends AbstractErrorException {

    public IntegrationErrorException(ErrorCode errorCode, String reason) {
        super(errorCode.getHttpStatus(), reason, errorCode.getCode());
    }

    public IntegrationErrorException(ErrorCode errorCode, String reason, List<Error> errors) {
        super(errorCode.getHttpStatus(), reason, errorCode.getCode(), errors);
    }
}
