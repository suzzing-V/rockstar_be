package suzzingv.suzzingv.rockstar.domain.notification.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.Notification;
import suzzingv.suzzingv.rockstar.domain.notification.domain.enums.NotificationType;
import suzzingv.suzzingv.rockstar.global.util.DateUtil;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long notificationUserId;

    private Long contentId;

    private String title;

    private String content;

    private NotificationType notificationType;

    private Boolean isRead;

    private Integer daysAgo;

    private Long hourAgo;

    private Long minuteAgo;

    private Long secondAgo;

    public static NotificationResponse of(Notification notification, boolean isRead, Long notificationUserId) {
        LocalDateTime createdAt = notification.getCreatedAt();
        int dayDiff = DateUtil.getDaysAgo(createdAt);
        long hourDiff = dayDiff == 0? DateUtil.getHoursAgo(createdAt) : 0;
        long minuteDiff = hourDiff == 0? DateUtil.getMinutesAgo(createdAt) : 0;
        long secondDiff = minuteDiff == 0? DateUtil.getSecondsAgo(createdAt) : 0;

        return NotificationResponse.builder()
                .content(notification.getContent())
                .contentId(notification.getContentId())
                .title(notification.getTitle())
                .notificationType(notification.getNotificationType())
                .daysAgo(dayDiff)
                .hourAgo(hourDiff)
                .minuteAgo(minuteDiff)
                .secondAgo(secondDiff)
                .isRead(isRead)
                .notificationUserId(notificationUserId)
                .build();
    }
}
