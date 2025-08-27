package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleRequestIdResponse {

    private Long requestId;

    public static ScheduleRequestIdResponse from(Long requestId) {
        return ScheduleRequestIdResponse.builder()
                .requestId(requestId)
                .build();
    }
}
