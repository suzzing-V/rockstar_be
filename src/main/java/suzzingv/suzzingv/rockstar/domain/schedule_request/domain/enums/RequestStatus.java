package suzzingv.suzzingv.rockstar.domain.schedule_request.domain.enums;

public enum RequestStatus {

    COMPLETED, PENDING;

    public static boolean isCompleted(RequestStatus requestStatus) {
        return requestStatus == COMPLETED;
    }
}
