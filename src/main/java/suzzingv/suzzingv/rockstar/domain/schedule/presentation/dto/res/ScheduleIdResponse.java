package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleIdResponse {

    private Long scheduleId;

    public static ScheduleIdResponse from(Long scheduleId) {
        return ScheduleIdResponse.builder()
                .scheduleId(scheduleId)
                .build();
    }
}
