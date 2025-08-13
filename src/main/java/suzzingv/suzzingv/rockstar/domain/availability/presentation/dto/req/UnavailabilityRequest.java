package suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.req;

import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.availability.domain.vo.TimeRange;

import java.time.LocalDate;
import java.util.List;

@Getter
public class UnavailabilityRequest {

    private Boolean isAllDay;

    private LocalDate date;

    private List<TimeRange> ranges;
}
