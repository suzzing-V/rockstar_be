package suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.availability.domain.entity.DayUnavailability;

@Getter
@Builder
public class UnavailabilityResponse {

    private Long userId;

    private Long dayUnavailabilityId;

    public static UnavailabilityResponse from(DayUnavailability dayUnavailability) {
        return UnavailabilityResponse.builder()
                .dayUnavailabilityId(dayUnavailability.getId())
                .userId(dayUnavailability.getUserId())
                .build();
    }
}
