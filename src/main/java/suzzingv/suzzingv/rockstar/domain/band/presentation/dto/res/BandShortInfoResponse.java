package suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BandShortInfoResponse {

    private Long bandId;
    private String bandName;
    private Boolean isManager;

    public static BandShortInfoResponse of(Long bandId, String bandName, Boolean isManager) {
        return BandShortInfoResponse.builder()
            .bandId(bandId)
            .bandName(bandName)
                .isManager(isManager)
            .build();
    }
}
