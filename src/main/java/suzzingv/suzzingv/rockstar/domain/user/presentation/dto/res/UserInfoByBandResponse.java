package suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@Getter
@Builder
public class UserInfoByBandResponse {

    private Long userId;

    private Boolean isManager;

    private String nickname;

    public static UserInfoByBandResponse of(User user, Boolean isManager) {
        return UserInfoByBandResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickName())
                .isManager(isManager)
                .build();
    }
}
