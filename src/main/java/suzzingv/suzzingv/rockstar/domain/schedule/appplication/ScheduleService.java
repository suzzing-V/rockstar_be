package suzzingv.suzzingv.rockstar.domain.schedule.appplication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;
import suzzingv.suzzingv.rockstar.domain.schedule.exception.ScheduleException;
import suzzingv.suzzingv.rockstar.domain.schedule.infrastructure.ScheduleRepository;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.req.ScheduleRequest;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleIdResponse;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleListResponse;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleResponse;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleShortResponse;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final BandService bandService;

    @Transactional(readOnly = true)
    public ScheduleListResponse getByBand(Long userId, Long bandId, int page, int size) {
        bandService.isBandMember(bandId, userId);
        Band band = bandService.findById(bandId);
        boolean isManager = band.getManagerId().equals(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Schedule> schedulePage = scheduleRepository.findByBandIdOrderByStartDateDesc(bandId, pageable);
        List<ScheduleShortResponse> scheduleList = schedulePage.map(ScheduleShortResponse::from).getContent();

        return ScheduleListResponse.of(scheduleList, isManager);
    }

    public ScheduleIdResponse createSchedule(Long userId, ScheduleRequest request) {
        Band band = bandService.findById(request.getBandId());
        bandService.isManager(userId, band.getManagerId());

        LocalDateTime startDateTime = LocalDateTime.of(
                request.getStartYear(),
                request.getStartMonth(),
                request.getStartDay(),
                request.getStartHour(),
                request.getStartMinute()
        );
        String dayOfWeeks = startDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
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

        return ScheduleIdResponse.from(schedule.getId());
    }

    private void isEndBeforeStart(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(endDateTime.isBefore(startDateTime)) {
            throw new ScheduleException(ErrorCode.END_DATE_BEFORE_START_DATE);
        }
    }

    public ScheduleIdResponse updateSchedule(Long scheduleId, ScheduleRequest request) {
        Schedule schedule = findById(scheduleId);
        LocalDateTime startDateTime = LocalDateTime.of(
                request.getStartYear(),
                request.getStartMonth(),
                request.getStartDay(),
                request.getStartHour(),
                request.getStartMinute()
        );
        String dayOfWeeks = startDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
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

        schedule.changeDescription(request.getDescription());
        schedule.changeDayDiff(dayDiff);
        schedule.changeDayOfWeek(dayOfWeeks);
        schedule.changeStartDate(startDateTime);
        schedule.changeEndDate(endDateTime);

        return ScheduleIdResponse.from(scheduleId);
    }

    private Schedule findById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
        return schedule;
    }

    public ScheduleResponse getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
        return ScheduleResponse.from(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = findById(scheduleId);
        scheduleRepository.delete(schedule);
    }
}
