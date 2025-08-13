package suzzingv.suzzingv.rockstar.domain.schedule.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByBandIdOrderByStartDateDesc(Long bandId, Pageable pageable);

    void deleteByBandId(Long bandId);
}
