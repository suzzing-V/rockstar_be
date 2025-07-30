package suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvitationResponse {

    private Long userId;

    private Long bandId;

    public static InvitationResponse of(Long userId, Long bandId) {
        return InvitationResponse.builder()
                .bandId(bandId)
                .userId(userId)
                .build();
    }
}
