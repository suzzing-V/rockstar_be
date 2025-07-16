package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;

@Getter
@Builder
public class ScheduleResponse {

    private Long scheduleId;

    private Integer startYear;

    private Integer startMonth;

    private Integer startDay;

    private Integer startHour;

    private Integer startMinute;

    private Integer endHour;

    private Integer endMinute;

    private String dayOfWeek;

    private Long dayDiff;

    private String description;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getId())
                .startYear(schedule.getStartDate().getYear())
                .startMonth(schedule.getStartDate().getMonthValue())
                .startDay(schedule.getStartDate().getDayOfMonth())
                .startHour(schedule.getStartDate().getHour())
                .startMinute(schedule.getStartDate().getMinute())
                .endHour(schedule.getEndDate().getHour())
                .endMinute(schedule.getEndDate().getMinute())
                .dayOfWeek(schedule.getDayOfWeek())
                .dayDiff(schedule.getDayDiff())
                .description(schedule.getDescription())
                .build();
    }
}
