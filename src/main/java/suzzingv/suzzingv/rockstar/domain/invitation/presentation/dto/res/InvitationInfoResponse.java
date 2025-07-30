package suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;

@Getter
@Builder
public class InvitationInfoResponse {

    private Long bandId;

    private String bandName;

    public static InvitationInfoResponse from(Band band) {
        return InvitationInfoResponse.builder()
                .bandId(band.getId())
                .bandName(band.getName())
                .build();
    }
}
