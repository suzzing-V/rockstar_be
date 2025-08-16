package suzzingv.suzzingv.rockstar.domain.availability.instructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.DayUnavailability;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.UnavailableBlock;
import suzzingv.suzzingv.rockstar.domain.availability.instructure.projection.SlotAvailabilityProjection;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

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

    @Query(value = """
WITH params AS (
  SELECT 
    CAST(:dayStart AS timestamptz) AS day_start,
    CAST(:durationMin AS int)      AS dur_min,
    CAST(:memberCount AS int)      AS member_count
),
slots AS (
  SELECT 
    generate_series(
      p.day_start,
      p.day_start + interval '1 day' - interval '30 min',
      interval '30 min'
    ) AS slot_start,
    p.dur_min,
    p.member_count
  FROM params p
)
SELECT 
  s.slot_start                               AS slot_start,
  s.slot_start + make_interval(mins => s.dur_min) AS slot_end,
  (s.member_count - COALESCE(c.conflict_cnt, 0))  AS available_count
FROM slots s
LEFT JOIN LATERAL (
  SELECT COUNT(DISTINCT ub.user_id) AS conflict_cnt
  FROM unavailable_block_tb ub
  WHERE ub.user_id IN (:memberIds)  -- ✅ 배열 캐스팅 없이 JPA가 자동 확장
    AND ub.period && tstzrange(
          s.slot_start,
          s.slot_start + make_interval(mins => s.dur_min),
          '[)'
        )
) c ON TRUE
ORDER BY available_count DESC, s.slot_start ASC
""", nativeQuery = true)
    List<SlotAvailabilityProjection> findBestSlots(
            @Param("dayStart") OffsetDateTime dayStartKst,
            @Param("durationMin") int durationMin,
            @Param("memberCount") int memberCount,
            @Param("memberIds") List<Long> memberIds
    );
}
