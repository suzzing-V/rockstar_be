package suzzingv.suzzingv.rockstar.domain.user.application.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

/**
 * [Strategy Pattern] ConcreteStrategy
 * 기존 사용자를 조회하는 구체적인 전략 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class ExistingUserStrategy implements UserCreationStrategy {
    private final UserRepository userRepository;

    /**
     * UserService에 있던 기존 유저 조회 로직을 그대로 가져와 구현합니다.
     * @param request 로그인 요청 DTO
     * @return 조회된 User 엔티티
     */
    @Override
    public User processUser(CodeRequest request) {
        return userRepository.findByPhoneNum(request.getPhoneNum())
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }
}
