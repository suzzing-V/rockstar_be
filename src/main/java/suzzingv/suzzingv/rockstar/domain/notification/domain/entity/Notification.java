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
@Table(name = "notification_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    private Notification(String title, String content, Long contentId, NotificationType notificationType) {
        this.title = title;
        this.content = content;
        this.contentId = contentId;
        this.notificationType = notificationType;
    }
}
