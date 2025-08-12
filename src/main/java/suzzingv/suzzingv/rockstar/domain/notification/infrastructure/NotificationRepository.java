package suzzingv.suzzingv.rockstar.domain.notification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.Notification;
import suzzingv.suzzingv.rockstar.global.db.DBMarkers.MainRepository;

@MainRepository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
