package suzzingv.suzzingv.authservice.security.exception;

import suzzingv.suzzingv.commonmodule.response.exceptionClass.CustomException;
import suzzingv.suzzingv.commonmodule.response.properties.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
