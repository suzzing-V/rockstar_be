package suzzingv.suzzingv.rockstar.domain.notification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
