package suzzingv.suzzingv.rockstar.domain.schedule_request.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequest;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequestAssignees;

import java.util.Optional;

public interface ScheduleRequestAssigneesRepository extends JpaRepository<ScheduleRequestAssignees, Long> {

    Optional<ScheduleRequestAssignees> findByRequestIdAndUserId(Long requestId, Long userId);
}
