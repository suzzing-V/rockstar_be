package suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req;

import lombok.Getter;

@Getter
public class CodeRequest {

    private String code;

    private String phoneNum;

    private Boolean isNew;
}
