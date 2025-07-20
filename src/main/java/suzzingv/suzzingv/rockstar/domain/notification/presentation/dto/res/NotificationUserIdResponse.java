package suzzingv.suzzingv.rockstar.domain.notification.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationUserIdResponse {

    private Long notificationId;

    public static NotificationUserIdResponse from(Long notificationId) {
        return NotificationUserIdResponse.builder()
                .notificationId(notificationId)
                .build();
    }
}
