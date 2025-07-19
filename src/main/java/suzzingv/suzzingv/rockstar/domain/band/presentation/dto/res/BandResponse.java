package suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;

@Getter
@Builder
public class BandResponse {

    private Long bandId;

    private String name;

    private Long managerId;

    public static BandResponse from(Band band) {
        return BandResponse.builder()
            .bandId(band.getId())
                .name(band.getName())
                .managerId(band.getManagerId())
            .build();
    }
}
