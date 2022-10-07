package br.com.calcred.api.exception;

import br.com.calcred.api.integration.crivo.enumeration.ErrorCode;

public class BusinessErrorException extends AbstractErrorException {

    public BusinessErrorException(final ErrorCode errorCode, final String reason) {
        super(errorCode.getHttpStatus(), reason, errorCode.getCode());
    }

     public BusinessErrorException(final ErrorCode errorCode, final String reason, final Throwable cause) {
        super(errorCode.getHttpStatus(), reason, errorCode.getCode(), cause);
    }
}
