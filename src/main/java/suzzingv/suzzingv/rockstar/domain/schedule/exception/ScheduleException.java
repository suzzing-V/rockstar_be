package suzzingv.suzzingv.rockstar.domain.schedule.exception;


import suzzingv.suzzingv.rockstar.global.response.exceptionClass.CustomException;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

public class ScheduleException extends CustomException {

    public ScheduleException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ScheduleException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
