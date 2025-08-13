package suzzingv.suzzingv.rockstar.domain.user.application.service.strategy;

import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.CodeRequest;

/**
 * [Strategy Pattern]
 * 사용자를 처리(생성 또는 조회)하는 전략을 정의하는 인터페이스입니다.
 * 이 인터페이스를 통해 AuthService는 실제 처리 로직을 알 필요 없이,
 * 주어진 상황에 맞는 전략을 실행하기만 하면 됩니다.
 */
public interface UserCreationStrategy {
    User processUser(CodeRequest request);
}
