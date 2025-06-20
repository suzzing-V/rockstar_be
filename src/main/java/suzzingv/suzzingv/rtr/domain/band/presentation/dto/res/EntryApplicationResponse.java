package suzzingv.suzzingv.rtr.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EntryApplicationResponse {

    private Long userId;
    private String userNickname;

    public static EntryApplicationResponse of(Long userId, String userNickname) {
        return EntryApplicationResponse.builder()
                .userId(userId)
                .userNickname(userNickname)
                .build();
    }
}
