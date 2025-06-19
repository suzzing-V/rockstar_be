package suzzingv.suzzingv.rtr.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EntryApplicationResponse {

    private String userNickname;

    public static EntryApplicationResponse of(String userNickname) {
        return EntryApplicationResponse.builder()
                .userNickname(userNickname)
                .build();
    }
}
