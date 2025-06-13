package suzzingv.suzzingv.rtr.domain.user.exception;


import suzzingv.suzzingv.rtr.global.response.exceptionClass.CustomException;
import suzzingv.suzzingv.rtr.global.response.properties.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
