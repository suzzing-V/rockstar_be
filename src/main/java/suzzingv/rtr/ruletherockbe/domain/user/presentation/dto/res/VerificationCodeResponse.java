package suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationCodeResponse {

    private String code;
}
