package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@Getter
@Builder
public class UserSummaryDto {

    private Long userId;

    private String nickname;

    public static UserSummaryDto from(User user) {
        return UserSummaryDto.builder()
                .userId(user.getId())
                .nickname(user.getNickName())
                .build();
    }
}
