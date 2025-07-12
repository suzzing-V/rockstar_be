package suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String nickname;

    public static LoginResponse of(String accessToken, String refreshToken, Long userId, String nickname) {
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .userId(userId)
                .nickname(nickname)
            .build();
    }
}
