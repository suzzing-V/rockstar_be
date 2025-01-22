package suzzingv.rtr.ruletherockbe.global.security.exception;

import suzzingv.rtr.ruletherockbe.global.response.exceptionClass.CustomException;
import suzzingv.rtr.ruletherockbe.global.response.properties.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
