package suzzingv.suzzingv.rockstar.domain.user.application.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.UserFcm;
import suzzingv.suzzingv.rockstar.domain.user.domain.enums.Role;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserFcmRepository;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.CodeRequest;

/**
 * [Strategy Pattern] ConcreteStrategy
 * 신규 사용자를 생성하는 구체적인 전략 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class NewUserStrategy implements UserCreationStrategy {
    private final UserRepository userRepository;
    private final UserFcmRepository userFcmRepository;

    /**
     * UserService에 있던 신규 유저 생성 로직을 그대로 가져와 구현합니다.
     * @param request 로그인 요청 DTO
     * @return 생성된 User 엔티티
     */
    @Override
    public User processUser(CodeRequest request) {
        User user = User.builder()
            .phoneNum(request.getPhoneNum())
            .role(Role.USER)
            .build();
        User savedUser = userRepository.save(user);

        UserFcm userFcm = UserFcm.builder()
                .userId(savedUser.getId())
                .build();
        userFcmRepository.save(userFcm);

        return savedUser;
    }
}
