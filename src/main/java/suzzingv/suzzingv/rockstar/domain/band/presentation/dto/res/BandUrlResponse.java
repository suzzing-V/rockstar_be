package suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;

@Getter
@Builder
public class BandUrlResponse {

    private Long bandId;

    private String url;

    public static BandUrlResponse from(Band band) {
        return BandUrlResponse.builder()
                .bandId(band.getId())
                .url(band.getInvitationUrl())
                .build();
    }
}
