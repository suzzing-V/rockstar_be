package suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BandIdResponse {

    private Long bandId;

    public static BandIdResponse from(Long bandId) {
        return BandIdResponse.builder()
                .bandId(bandId)
                .build();
    }
}
