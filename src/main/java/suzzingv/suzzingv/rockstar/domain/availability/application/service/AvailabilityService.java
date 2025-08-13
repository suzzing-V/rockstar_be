package suzzingv.suzzingv.rockstar.domain.availability.application.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.DayUnavailability;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.UnavailableBlock;
import suzzingv.suzzingv.rockstar.domain.availability.domain.vo.TimeRange;
import suzzingv.suzzingv.rockstar.domain.availability.instructure.DayUnavailabilityRepository;
import suzzingv.suzzingv.rockstar.domain.availability.instructure.UnavailableBlockRepository;
import suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.req.UnavailabilityRequest;
import suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.res.UnavailabilityResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AvailabilityService {

    private final DayUnavailabilityRepository dayUnavailabilityRepository;
    private final UnavailableBlockRepository unavailableBlockRepository;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public UnavailabilityResponse createUnAvailableDay(Long userId, UnavailabilityRequest request) {
        dayUnavailabilityRepository.deleteByUserIdAndDate(userId, request.getDate());
        DayUnavailability dayUnavailability = DayUnavailability.builder()
                .userId(userId)
                .date(request.getDate())
                .isAllDay(request.getIsAllDay())
                .range(request.getRanges())
                .build();
        dayUnavailabilityRepository.save(dayUnavailability);

        createUnavailableBlock(userId, request);

        return UnavailabilityResponse.from(dayUnavailability);
    }

    private boolean createUnavailableBlock(Long userId, UnavailabilityRequest request) {
        // KST 기준 하루 박스
        ZonedDateTime dayStart = request.getDate().atStartOfDay(KST);
        ZonedDateTime dayEnd   = request.getDate().plusDays(1).atStartOfDay(KST);

        // 해당 날짜와 겹치는 기존 블록 삭제 (덮어쓰기)
        unavailableBlockRepository.deleteAllOverlapping(userId, dayStart.toInstant(), dayEnd.toInstant());

        if (Boolean.TRUE.equals(request.getIsAllDay())) {
            // 종일 불가
            UnavailableBlock unavailableBlock = UnavailableBlock.builder()
                    .userId(userId)
                    .period(Range.closedOpen(dayStart, dayEnd))
                    .build();
            unavailableBlockRepository.save(unavailableBlock);
            return true;
        }

        for (TimeRange r : request.getRanges()) {
            int s = Math.max(0, Math.min(r.getStartMin(), 1440));
            int e = Math.max(0, Math.min(r.getEndMin(),   1440));
            if (e <= s) continue;

            ZonedDateTime start = toZonedDateTime(request.getDate(), s);     // KST 고정
            ZonedDateTime end   = (e == 1440) ? dayEnd : toZonedDateTime(request.getDate(), e);

            UnavailableBlock unavailableBlock = UnavailableBlock.builder()
                    .userId(userId)
                    .period(Range.closedOpen(start, end))
                    .build();
            unavailableBlockRepository.save(unavailableBlock);
        }
        return false;
    }

    private ZonedDateTime toZonedDateTime(LocalDate date, int minutes) {
        LocalTime time = LocalTime.ofSecondOfDay(minutes * 60L);
        return date.atTime(time).atZone(KST);
    }
}
