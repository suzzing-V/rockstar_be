package suzzingv.suzzingv.rockstar.domain.schedule_request.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.NotificationUser;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequest;

public interface ScheduleRequestRepository extends JpaRepository<ScheduleRequest, Long> {

    Page<ScheduleRequest> findByBandId(Long bandId, PageRequest pageable);
}
