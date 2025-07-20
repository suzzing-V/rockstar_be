package suzzingv.suzzingv.rockstar.domain.notification.exception;

import suzzingv.suzzingv.rockstar.global.response.exceptionClass.CustomException;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

public class NotificationException extends CustomException {

    public NotificationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotificationException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}