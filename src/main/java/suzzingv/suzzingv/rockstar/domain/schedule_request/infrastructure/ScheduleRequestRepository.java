package suzzingv.suzzingv.rockstar.domain.schedule_request.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequest;

public interface ScheduleRequestRepository extends JpaRepository<ScheduleRequest, Long> {
}
