package suzzingv.suzzingv.rockstar.domain.notification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
