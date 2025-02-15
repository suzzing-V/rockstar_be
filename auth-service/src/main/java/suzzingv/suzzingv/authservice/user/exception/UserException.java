package suzzingv.suzzingv.authservice.user.exception;

import suzzingv.suzzingv.commonmodule.response.exceptionClass.CustomException;
import suzzingv.suzzingv.commonmodule.response.properties.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
