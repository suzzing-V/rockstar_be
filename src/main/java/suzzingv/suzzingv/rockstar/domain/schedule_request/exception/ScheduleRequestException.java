package suzzingv.suzzingv.rockstar.domain.schedule_request.exception;

import suzzingv.suzzingv.rockstar.global.response.exceptionClass.CustomException;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

public class ScheduleRequestException extends CustomException {

    public ScheduleRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ScheduleRequestException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
