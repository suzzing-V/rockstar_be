package suzzingv.suzzingv.rtr.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;

    public static LoginResponse of(String accessToken, String refreshToken, Long userId) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .build();
    }
}
