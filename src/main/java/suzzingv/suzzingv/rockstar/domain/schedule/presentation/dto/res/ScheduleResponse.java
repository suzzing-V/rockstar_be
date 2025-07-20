package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;

@Getter
@Builder
public class ScheduleResponse {

    private Boolean isManager;

    private Long scheduleId;

    private Integer startYear;

    private Integer startMonth;

    private Integer startDay;

    private Integer startHour;

    private Integer startMinute;

    private Integer endYear;

    private Integer endMonth;

    private Integer endDay;

    private Integer endHour;

    private Integer endMinute;

    private String dayOfWeek;

    private String description;

    public static ScheduleResponse of(Schedule schedule, boolean isManager) {
        return ScheduleResponse.builder()
                .isManager(isManager)
                .scheduleId(schedule.getId())
                .startYear(schedule.getStartDate().getYear())
                .startMonth(schedule.getStartDate().getMonthValue())
                .startDay(schedule.getStartDate().getDayOfMonth())
                .startHour(schedule.getStartDate().getHour())
                .startMinute(schedule.getStartDate().getMinute())
                .endYear(schedule.getEndDate().getYear())
                .endMonth(schedule.getEndDate().getMonthValue())
                .endDay(schedule.getEndDate().getDayOfMonth())
                .endHour(schedule.getEndDate().getHour())
                .endMinute(schedule.getEndDate().getMinute())
                .dayOfWeek(schedule.getDayOfWeek())
                .description(schedule.getDescription())
                .build();
    }

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getId())
                .startYear(schedule.getStartDate().getYear())
                .startMonth(schedule.getStartDate().getMonthValue())
                .startDay(schedule.getStartDate().getDayOfMonth())
                .startHour(schedule.getStartDate().getHour())
                .startMinute(schedule.getStartDate().getMinute())
                .endYear(schedule.getEndDate().getYear())
                .endMonth(schedule.getEndDate().getMonthValue())
                .endDay(schedule.getEndDate().getDayOfMonth())
                .endHour(schedule.getEndDate().getHour())
                .endMinute(schedule.getEndDate().getMinute())
                .dayOfWeek(schedule.getDayOfWeek())
                .description(schedule.getDescription())
                .build();
    }
}
