package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ScheduleRequestResponse {

    private Long requestId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer completedCount;

    private Integer totalCount;

    private List<UserSummaryDto> pendingMembers;

    public static ScheduleRequestResponse of(ScheduleRequest scheduleRequest, int totalCount, List<UserSummaryDto> pendingMembers) {
        return ScheduleRequestResponse.builder()
                .requestId(scheduleRequest.getId())
                .startDate(scheduleRequest.getStartDate())
                .endDate(scheduleRequest.getEndDate())
                .completedCount(totalCount - pendingMembers.size())
                .totalCount(totalCount)
                .pendingMembers(pendingMembers)
                .build();
    }

}
