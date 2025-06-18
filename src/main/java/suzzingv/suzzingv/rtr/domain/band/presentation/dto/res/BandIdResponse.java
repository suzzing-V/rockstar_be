package suzzingv.suzzingv.rtr.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BandIdResponse {

    private Long id;

    public static BandIdResponse from(Long bandId) {
        return BandIdResponse.builder()
            .id(bandId)
            .build();
    }
}
