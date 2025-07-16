package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.req;

import lombok.Getter;

@Getter
public class ScheduleRequest {

    private Long bandId;

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

    private String description;

}
