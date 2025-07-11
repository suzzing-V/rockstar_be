package suzzingv.suzzingv.rockstar.global.security.exception;

import suzzingv.suzzingv.rockstar.global.response.exceptionClass.CustomException;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
