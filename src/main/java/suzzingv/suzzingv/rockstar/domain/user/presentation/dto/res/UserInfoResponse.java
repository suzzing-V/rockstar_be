package suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@Getter
@Builder
public class UserInfoResponse {

    private Long userId;

    private String phoneNum;

    private String nickname;

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .phoneNum(user.getPhoneNum())
                .nickname(user.getNickName())
                .build();
    }
}
