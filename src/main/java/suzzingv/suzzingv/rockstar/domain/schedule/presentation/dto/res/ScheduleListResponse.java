package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;

import java.util.List;

@Getter
@Builder
public class ScheduleListResponse {

    private Boolean isManager;

    private List<ScheduleResponse> scheduleList;

    public static ScheduleListResponse of(List<ScheduleResponse> scheduleList, boolean isManager) {
        return ScheduleListResponse.builder()
                .scheduleList(scheduleList)
                .isManager(isManager)
                .build();
    }
}
