package suzzingv.suzzingv.rockstar.domain.availability.instructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.DayUnavailability;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.UnavailableBlock;

import java.time.Instant;

public interface UnavailableBlockRepository extends JpaRepository<UnavailableBlock, Long> {

    @Modifying
    @Query(value = """
        DELETE FROM unavailable_block_tb
        WHERE user_id = :userId
          AND period && tstzrange(:dayStart, :dayEnd, '[)')
        """, nativeQuery = true)
    void deleteAllOverlapping(Long userId,
                              Instant dayStart,
                              Instant dayEnd);
}
