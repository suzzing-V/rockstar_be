package suzzingv.suzzingv.rtr.domain.band.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EntryAcceptResponse {

    private Long userId;

    public static EntryAcceptResponse from(Long userId) {
        return EntryAcceptResponse.builder()
                .userId(userId)
                .build();
    }
}
