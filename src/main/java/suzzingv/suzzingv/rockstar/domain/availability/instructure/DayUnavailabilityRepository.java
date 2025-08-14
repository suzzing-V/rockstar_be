package suzzingv.suzzingv.rockstar.domain.availability.instructure;

import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.DayUnavailability;

import java.time.LocalDate;
import java.util.List;

public interface DayUnavailabilityRepository extends JpaRepository<DayUnavailability, Long> {
    void deleteByUserIdAndDate(Long userId, LocalDate date);
    List<DayUnavailability> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
