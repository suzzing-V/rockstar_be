package suzzingv.suzzingv.authservice.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationCodeResponse {

    private String code;
}
