package suzzingv.suzzingv.rockstar.domain.notification.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.NotificationUser;

public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {
    Page<NotificationUser> findByUserId(Long userId, PageRequest pageable);
}
