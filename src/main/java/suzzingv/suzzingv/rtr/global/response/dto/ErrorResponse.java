package suzzingv.suzzingv.rtr.global.response.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import suzzingv.suzzingv.rtr.global.response.properties.ErrorCode;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String statusCodeName;
    private final String code;
    private final String message;
    private final String runtimeValue;

    public static ResponseEntity<ErrorResponse> toResponseEntity(
            ErrorCode errorCode, String runtimeValue
    ) {
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .statusCodeName(errorCode.getHttpStatus().name())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .runtimeValue(runtimeValue)
                .build()
            );
    }
}
