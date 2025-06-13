package suzzingv.suzzingv.rtr.global.response.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import suzzingv.suzzingv.rtr.global.response.exceptionClass.CustomException;

@UtilityClass
public class GrpcStatusMapper {

    public static StatusRuntimeException toGrpcError(CustomException e) {
        HttpStatus httpStatus = e.getErrorCode().getHttpStatus();
        String message = e.getErrorCode().getMessage();
        
        Status.Code code = switch (httpStatus) {
            case BAD_REQUEST -> Status.Code.INVALID_ARGUMENT;
            case UNAUTHORIZED -> Status.Code.UNAUTHENTICATED;
            case FORBIDDEN -> Status.Code.PERMISSION_DENIED;
            case NOT_FOUND -> Status.Code.NOT_FOUND;
            case CONFLICT -> Status.Code.ALREADY_EXISTS;
            case INTERNAL_SERVER_ERROR -> Status.Code.INTERNAL;
            default -> Status.Code.UNKNOWN;
        };

        return Status.fromCode(code).withDescription(message).asRuntimeException();
    }
}
