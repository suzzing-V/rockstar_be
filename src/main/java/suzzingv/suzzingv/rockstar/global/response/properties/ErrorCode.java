package suzzingv.suzzingv.rockstar.global.response.properties;

import com.google.api.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    REFRESH_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "refresh token이 필요합니다."),
    ACCESS_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "access token이 필요합니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    VERIFICATION_CODE_INCORRECT(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다."),
    NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    END_DATE_BEFORE_START_DATE(HttpStatus.BAD_REQUEST, "종료 날짜가 시작 날짜보다 이릅니다."),
    MANAGER_CANT_WITHDRAW(HttpStatus.BAD_REQUEST, "관리자는 밴드를 탈퇴할 수 없습니다."),

    // 401
    SECURITY_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SECURITY_INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "refresh token이 유효하지 않습니다."),

    // 403
    SECURITY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    MANAGER_REQUIRED(HttpStatus.FORBIDDEN, "매니저 권한이 필요합니다."),

    // 404
    NOT_BAND_MEMBER(HttpStatus.NOT_FOUND, "해당 밴드의 멤버가 아닙니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    BAND_NOT_FOUND(NOT_FOUND, "존재하지 않는 밴드입니다."),
    SCHEDULE_NOT_FOUND(NOT_FOUND, "존재하지 않는 스케줄입니다."),
    NOTIFICATION_NOT_FOUND(NOT_FOUND, "존재하지 않는 알림입니다."),
    NOTIFICATION_USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 유저 알림입니다."),
    USER_FCM_NOT_FOUND(NOT_FOUND, "유저의 fcm 토큰 정보가 존재하지 않습니다."),
    SCHEDULE_REQUEST_ASSIGNEES_NOT_FOUND(NOT_FOUND, "일정 갱신 요청의 멤버 정보가 존재하지 않습니다."),
    SCHEDULE_REQUEST_NOT_FOUND(NOT_FOUND, "일정 갱신 요청이 존재하지 않습니다."),

    // 500
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다."),
    SMS_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "문자 전송 중 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
