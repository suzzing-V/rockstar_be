package suzzingv.suzzingv.rtr.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BandResponse {

    private Long bandId;

    public static BandResponse from(Long bandId) {
        return BandResponse.builder()
            .bandId(bandId)
            .build();
    }
}
