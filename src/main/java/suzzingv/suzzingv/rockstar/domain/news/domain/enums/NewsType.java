package suzzingv.suzzingv.rockstar.domain.news.domain.enums;

public enum NewsType {

    SCHEDULE_CREATED("일정이 생성되었습니다."),
    SCHEDULE_CHANGED("일정이 변경되었습니다."),
    SCHEDULE_DELETED("일정이 삭제되었습니다.");

    private final String message;

    NewsType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
