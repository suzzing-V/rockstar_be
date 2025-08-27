package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.req;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleRequestRequest {

    private Long bandId;

    private LocalDate startDate;

    private LocalDate endDate;
}
