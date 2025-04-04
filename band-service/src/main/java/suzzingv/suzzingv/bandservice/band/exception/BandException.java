package suzzingv.suzzingv.bandservice.band.exception;

import suzzingv.suzzingv.commonmodule.response.exceptionClass.CustomException;
import suzzingv.suzzingv.commonmodule.response.properties.ErrorCode;

public class BandException extends CustomException {

    public BandException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BandException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
