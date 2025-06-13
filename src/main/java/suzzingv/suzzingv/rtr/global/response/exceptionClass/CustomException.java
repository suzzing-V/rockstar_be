package suzzingv.suzzingv.rtr.global.response.exceptionClass;

import lombok.Getter;
import suzzingv.suzzingv.rtr.global.response.properties.ErrorCode;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String runtimeValue;

    public CustomException(ErrorCode errorCode) {
        this(errorCode, "runtimeValue가 존재 하지 않습니다.");
    }

    public CustomException(ErrorCode errorCode, String runtimeValue) {
        this.errorCode = errorCode;
        this.runtimeValue = runtimeValue;
    }
}
