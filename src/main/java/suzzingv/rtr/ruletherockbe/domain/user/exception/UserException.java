package suzzingv.rtr.ruletherockbe.domain.user.exception;


import suzzingv.rtr.ruletherockbe.global.response.exceptionClass.CustomException;
import suzzingv.rtr.ruletherockbe.global.response.properties.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
