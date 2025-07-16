package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;

@Getter
@Builder
public class ScheduleCreateResponse {

    private Long scheduleId;

    public static ScheduleCreateResponse from(Long scheduleId) {
        return ScheduleCreateResponse.builder()
                .scheduleId(scheduleId)
                .build();
    }
}
