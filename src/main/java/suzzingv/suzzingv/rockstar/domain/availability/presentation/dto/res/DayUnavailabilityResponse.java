package suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.DayUnavailability;
import suzzingv.suzzingv.rockstar.domain.availability.domain.vo.TimeRange;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DayUnavailabilityResponse {

    private Long id;
    private LocalDate date;
    private String tz;
    private Boolean isAllDay;
    private List<TimeRange> ranges;

    public static DayUnavailabilityResponse from(DayUnavailability dayUnavailability) {
        return DayUnavailabilityResponse.builder()
                .id(dayUnavailability.getId())
                .date(dayUnavailability.getDate())
                .tz(dayUnavailability.getTz())
                .isAllDay(dayUnavailability.isAllDay())
                .ranges(dayUnavailability.getRange())
                .build();
    }
}