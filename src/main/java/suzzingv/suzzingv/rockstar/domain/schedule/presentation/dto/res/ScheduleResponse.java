package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;

@Getter
@Builder
public class ScheduleResponse {

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer hour;

    private Integer minute;

    private String dayOfWeek;

    private Long dayDiff;

    private String description;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .year(schedule.getStartDate().getYear())
                .month(schedule.getStartDate().getMonthValue())
                .day(schedule.getStartDate().getDayOfMonth())
                .hour(schedule.getStartDate().getHour())
                .minute(schedule.getStartDate().getMinute())
                .dayOfWeek(schedule.getDayOfWeek())
                .dayDiff(schedule.getDayDiff())
                .description(schedule.getDescription())
                .build();
    }
}
