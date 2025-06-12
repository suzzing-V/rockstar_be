package suzzingv.suzzingv.rtr.domain.band.exception;


import suzzingv.suzzingv.rtr.global.response.exceptionClass.CustomException;
import suzzingv.suzzingv.rtr.global.response.properties.ErrorCode;

public class BandException extends CustomException {

    public BandException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BandException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
