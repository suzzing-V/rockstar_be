package suzzingv.suzzingv.rockstar.domain.user.application.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * [Strategy Pattern] Factory
 * isNew 값에 따라 적절한 UserCreationStrategy 구현체를 반환하는 팩토리 클래스입니다.
 * 이를 통해 서비스 로직(컨텍스트)은 구체적인 전략 클래스에 대한 의존성을 제거할 수 있습니다.
 */
@Component
@RequiredArgsConstructor
public class UserStrategyFactory {
    private final NewUserStrategy newUserStrategy;
    private final ExistingUserStrategy existingUserStrategy;

    public UserCreationStrategy getStrategy(boolean isNew) {
        if (isNew) {
            return newUserStrategy;
        } else {
            return existingUserStrategy;
        }
    }
}
