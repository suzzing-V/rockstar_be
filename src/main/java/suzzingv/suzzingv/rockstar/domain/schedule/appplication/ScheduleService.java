package suzzingv.suzzingv.rockstar.domain.schedule.appplication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.SchedulingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;
import suzzingv.suzzingv.rockstar.domain.schedule.exception.ScheduleException;
import suzzingv.suzzingv.rockstar.domain.schedule.infrastructure.ScheduleRepository;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.req.ScheduleRequest;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleCreateResponse;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleResponse;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final BandService bandService;

    @Transactional(readOnly = true)
    public Page<ScheduleResponse> getByBand(Long userId, Long bandId, int page, int size) {
        bandService.isBandMember(bandId, userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Schedule> scheduleList = scheduleRepository.findByBandIdOrderByStartDateDesc(bandId, pageable);

        return scheduleList.map(ScheduleResponse::from);
    }

    public ScheduleCreateResponse createSchedule(Long userId, ScheduleRequest request) {
        Band band = bandService.findById(request.getBandId());
        bandService.isManager(userId, band.getManagerId());

        LocalDateTime startDateTime = LocalDateTime.of(
                request.getStartYear(),
                request.getStartMonth(),
                request.getStartDay(),
                request.getStartHour(),
                request.getStartMinute()
        );
        String dayOfWeeks = startDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        LocalDateTime endDateTime = LocalDateTime.of(
                request.getEndYear(),
                request.getEndMonth(),
                request.getEndDay(),
                request.getEndHour(),
                request.getEndMinute()
        );
        isEndBeforeStart(startDateTime, endDateTime);

        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        long dayDiff = ChronoUnit.DAYS.between(startDate, endDate);

        Schedule schedule = Schedule.builder()
                .bandId(request.getBandId())
                .startDate(startDateTime)
                .endDate(endDateTime)
                .dayOfWeek(dayOfWeeks)
                .dayDiff(dayDiff)
                .description(request.getDescription())
                .build();
        scheduleRepository.save(schedule);

        return ScheduleCreateResponse.from(schedule.getId());
    }

    private void isEndBeforeStart(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(endDateTime.isBefore(startDateTime)) {
            throw new ScheduleException(ErrorCode.END_DATE_BEFORE_START_DATE);
        }
    }
}
