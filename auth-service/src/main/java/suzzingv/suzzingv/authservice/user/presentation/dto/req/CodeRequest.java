package suzzingv.suzzingv.authservice.user.presentation.dto.req;

import lombok.Getter;

@Getter
public class CodeRequest {

    private String code;

    private String phoneNum;

    private Boolean isNew;
}
