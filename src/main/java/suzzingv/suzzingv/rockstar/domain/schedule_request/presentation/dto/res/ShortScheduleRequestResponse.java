package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class ShortScheduleRequestResponse {

    private Long requestId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    private Boolean isCompleted;

    public static ShortScheduleRequestResponse of(ScheduleRequest scheduleRequest, boolean isCompleted) {
        return ShortScheduleRequestResponse.builder()
                .requestId(scheduleRequest.getId())
                .startDate(scheduleRequest.getStartDate())
                .endDate(scheduleRequest.getEndDate())
                .createdAt(scheduleRequest.getCreatedAt())
                .isCompleted(isCompleted)
                .build();
    }
}
