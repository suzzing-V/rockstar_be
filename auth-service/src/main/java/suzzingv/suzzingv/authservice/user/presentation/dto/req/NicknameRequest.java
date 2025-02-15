package suzzingv.suzzingv.authservice.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class NicknameRequest {

    @NotEmpty
    private String nickname;
}
