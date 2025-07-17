package suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleListResponse {

    private Boolean isManager;

    private List<ScheduleShortResponse> scheduleList;

    public static ScheduleListResponse of(List<ScheduleShortResponse> scheduleList, boolean isManager) {
        return ScheduleListResponse.builder()
                .scheduleList(scheduleList)
                .isManager(isManager)
                .build();
    }
}
