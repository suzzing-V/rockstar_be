package suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class NicknameRequest {

    @NotEmpty
    private String nickname;
}
