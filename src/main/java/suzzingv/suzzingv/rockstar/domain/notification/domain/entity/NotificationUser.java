package suzzingv.suzzingv.rockstar.domain.notification.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import suzzingv.suzzingv.rockstar.domain.notification.domain.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_user_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NotificationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long notificationId;

    private Boolean isRead;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    private NotificationUser(Long userId, Long notificationId) {
        this.userId = userId;
        this.notificationId = notificationId;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
