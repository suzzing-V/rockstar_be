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

    private String description;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .year(schedule.getDate().getYear())
                .month(schedule.getDate().getMonthValue())
                .day(schedule.getDate().getDayOfMonth())
                .hour(schedule.getDate().getHour())
                .minute(schedule.getDate().getMinute())
                .dayOfWeek(schedule.getDayOfWeek())
                .description(schedule.getDescription())
                .build();
    }
}
