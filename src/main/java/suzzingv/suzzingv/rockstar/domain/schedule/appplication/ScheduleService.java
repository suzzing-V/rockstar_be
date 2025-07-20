package suzzingv.suzzingv.rockstar.domain.schedule.appplication;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandRepository;
import suzzingv.suzzingv.rockstar.domain.news.application.NewsService;
import suzzingv.suzzingv.rockstar.domain.news.domain.enums.NewsType;
import suzzingv.suzzingv.rockstar.domain.notification.application.NotificationService;
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
    private final NewsService newsService;
    private final BandRepository bandRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public ScheduleListResponse getByBand(Long userId, Long bandId, int page, int size) {
        Band band = findBandById(bandId);
        boolean isManager = band.getManagerId().equals(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Schedule> schedulePage = scheduleRepository.findByBandIdOrderByStartDateDesc(bandId, pageable);
        List<ScheduleShortResponse> scheduleList = schedulePage.map(ScheduleShortResponse::from).getContent();

        return ScheduleListResponse.of(scheduleList, isManager);
    }

    private Band findBandById(Long bandId) {
        Band band = bandRepository.findById(bandId)
                .orElseThrow(() -> new ScheduleException(ErrorCode.BAND_NOT_FOUND));
        return band;
    }

    public ScheduleIdResponse createSchedule(Long userId, ScheduleRequest request) {
        Band band = findBandById(request.getBandId());

        LocalDateTime startDateTime = startDateToLocalDateTime(request);
        String dayOfWeeks = startDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        LocalDateTime endDateTime = endDateToLocalDateTime(request);
        isEndBeforeStart(startDateTime, endDateTime);

        long dayDiff = getDayDiff(startDateTime, endDateTime);

        Schedule schedule = Schedule.builder()
                .bandId(request.getBandId())
                .startDate(startDateTime)
                .endDate(endDateTime)
                .dayOfWeek(dayOfWeeks)
                .dayDiff(dayDiff)
                .description(request.getDescription())
                .build();
        scheduleRepository.save(schedule);

        newsService.createNews(band.getId(), schedule.getId(), NewsType.SCHEDULE_CREATED, startDateTime);
        notificationService.createScheduleCreationNotification(band, schedule.getId(), startDateTime);
        return ScheduleIdResponse.from(schedule.getId());
    }

    private static long getDayDiff(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        long dayDiff = ChronoUnit.DAYS.between(startDate, endDate);
        return dayDiff;
    }

    @NotNull
    private static LocalDateTime endDateToLocalDateTime(ScheduleRequest request) {
        LocalDateTime endDateTime = LocalDateTime.of(
                request.getEndYear(),
                request.getEndMonth(),
                request.getEndDay(),
                request.getEndHour(),
                request.getEndMinute()
        );
        return endDateTime;
    }

    private LocalDateTime startDateToLocalDateTime(ScheduleRequest request) {
        LocalDateTime startDateTime = LocalDateTime.of(
                request.getStartYear(),
                request.getStartMonth(),
                request.getStartDay(),
                request.getStartHour(),
                request.getStartMinute()
        );
        return startDateTime;
    }

    private void isEndBeforeStart(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(endDateTime.isBefore(startDateTime)) {
            throw new ScheduleException(ErrorCode.END_DATE_BEFORE_START_DATE);
        }
    }

    public ScheduleIdResponse updateSchedule(Long scheduleId, ScheduleRequest request) {
        Schedule schedule = findById(scheduleId);
        LocalDateTime oldDateTime = schedule.getStartDate();
        LocalDateTime newStartDateTime = startDateToLocalDateTime(request);
        String dayOfWeeks = newStartDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        LocalDateTime endDateTime = endDateToLocalDateTime(request);
        isEndBeforeStart(newStartDateTime, endDateTime);

        long dayDiff = getDayDiff(newStartDateTime, endDateTime);

        schedule.changeDescription(request.getDescription());
        schedule.changeDayDiff(dayDiff);
        schedule.changeDayOfWeek(dayOfWeeks);
        schedule.changeStartDate(newStartDateTime);
        schedule.changeEndDate(endDateTime);

        Band band = findBandById(schedule.getBandId());
        newsService.createNews(schedule.getBandId(), schedule.getId(), NewsType.SCHEDULE_CHANGED, oldDateTime, newStartDateTime);
        notificationService.createScheduleUpdateNotification(band, scheduleId, oldDateTime);
        return ScheduleIdResponse.from(scheduleId);
    }

    private Schedule findById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
        return schedule;
    }

    public ScheduleResponse getScheduleOfBand(Long userId, Long scheduleId, Long bandId) {
        Band band = findBandById(bandId);
        boolean isManager = band.getManagerId().equals(userId);

        Schedule schedule = findById(scheduleId);
        return ScheduleResponse.of(schedule,isManager);
    }

    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = findById(scheduleId);
        scheduleRepository.delete(schedule);
        newsService.createNews(schedule.getBandId(), schedule.getId(), NewsType.SCHEDULE_DELETED, schedule.getStartDate());

        Band band = findBandById(schedule.getBandId());
        notificationService.createScheduleDeleteNotification(band, schedule.getStartDate());
    }

    public void deleteByBandId(Long bandId) {
        scheduleRepository.deleteByBandId(bandId);
    }

    public ScheduleResponse getSchedule(Long scheduleId) {
        Schedule schedule = findById(scheduleId);

        return ScheduleResponse.from(schedule);
    }
}
