package suzzingv.suzzingv.rtr.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BandNameResponse {

    private Long bandId;
    private String bandName;

    public static BandNameResponse of(Long bandId, String bandName) {
        return BandNameResponse.builder()
            .bandId(bandId)
            .bandName(bandName)
            .build();
    }
}
