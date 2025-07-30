package suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@Getter
@Builder
public class InvitationUserInfoResponse {

    private Long userId;

    private String nickname;

    private Boolean isPossible;

    public static InvitationUserInfoResponse of(User user, boolean isPossible) {
        return InvitationUserInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickName())
                .isPossible(isPossible)
                .build();
    }
}
