package suzzingv.suzzingv.rockstar.domain.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rockstar.domain.user.application.service.strategy.UserStrategyFactory;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.LoginResponse;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.TokenResponse;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.VerificationCodeResponse;
import suzzingv.suzzingv.rockstar.domain.user.util.VerificationCodeGenerator;
import suzzingv.suzzingv.rockstar.global.redis.RedisService;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;
import suzzingv.suzzingv.rockstar.global.security.jwt.service.JwtService;
import suzzingv.suzzingv.rockstar.global.sms.MessageUtils;
import suzzingv.suzzingv.rockstar.global.sms.SmsSender;

import java.time.Duration;
import java.util.Optional;

/**
 * [Service] 인증 관련 로직을 담당하는 서비스
 * SRP(단일 책임 원칙)에 따라 UserService에서 인증 관련 기능을 분리했습니다.
 * 로그인, 로그아웃, 토큰 재발급, SMS 인증 등의 책임을 가집니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final SmsSender smsSender;
    private final RedisService redisService;
    private final JwtService jwtService;
    private final UserStrategyFactory userStrategyFactory;

    private static final String VERIFICATION_PREFIX = "verify_";
    private static final Duration VERIFICATION_DURATION = Duration.ofMinutes(1);

    /**
     * SMS 인증 코드를 발송합니다.
     * @param request 휴대폰 번호, 신규 유저 여부
     * @return 발송된 인증 코드 (테스트용)
     */
    public VerificationCodeResponse sendVerificationCode(PhoneNumRequest request) {
        // 리팩토링 전: UserService에 있던 로직
        // 신규/기존 유저 여부에 따라 유저 존재 여부를 확인합니다.
        checkUserByNew(request);
        String verificationCode = VerificationCodeGenerator.getCode();

        // 테스트용 번호가 아닐 경우에만 실제 SMS를 발송하고 Redis에 코드를 저장합니다.
        if(!request.getPhoneNum().equals("12345678901")) {
//            sendCode(verificationCode, request.getPhoneNum());
            saveCode(request.getPhoneNum(), verificationCode);
        }
        return VerificationCodeResponse.builder()
            .code(verificationCode)
            .build();
    }

    /**
     * 로그인 또는 회원가입을 처리합니다.
     * @param request 휴대폰 번호, 인증 코드, 신규 유저 여부
     * @return Access/Refresh 토큰, 사용자 정보
     */
    public LoginResponse login(CodeRequest request) {
        // 리팩토링 전: UserService에 있던 로직
        // 1. 인증 코드가 올바른지 확인합니다.
        isCorrectCode(request.getPhoneNum(), request.getCode());

        // 리팩토링 후: 전략 패턴 적용
        // 2. UserStrategyFactory를 통해 적절한 전략(신규/기존)을 가져와 사용자를 처리합니다.
        // AuthService는 User를 어떻게 생성하거나 조회하는지에 대한 구체적인 방법을 알 필요가 없습니다.
        User user = userStrategyFactory.getStrategy(request.getIsNew()).processUser(request);

        // 3. JWT 토큰을 생성하고 저장합니다.
        String accessToken = jwtService.createAccessToken(request.getPhoneNum());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, request.getPhoneNum());

        return LoginResponse.of(accessToken, refreshToken, user.getId(), user.getNickName());
    }

    /**
     * Access/Refresh 토큰을 재발급합니다.
     * @param refreshToken 유효한 Refresh 토큰
     * @return 새로 발급된 Access/Refresh 토큰
     */
    public TokenResponse reissueToken(String refreshToken) {
        // 리팩토링 전: UserService에 있던 로직
        String phoneNum = jwtService.checkRefreshToken(refreshToken);

        String newAccessToken = jwtService.createAccessToken(phoneNum);
        String newRefreshToken = jwtService.reissueRefreshToken(refreshToken, phoneNum);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    /**
     * 로그아웃 처리 (토큰 무효화)
     * @param accessToken 무효화할 Access 토큰
     * @param refreshToken 무효화할 Refresh 토큰
     */
    public void logout(String accessToken, String refreshToken) {
        // 리팩토링 전: UserController에 있던 로직
        jwtService.invalidTokens(accessToken, refreshToken);
    }

    // ========= Private Helper Methods ========= //

    private void checkUserByNew(PhoneNumRequest request) {
        if (request.getIsNew()) {
            checkNewUser(request.getPhoneNum());
        } else {
            findByPhoneNum(request.getPhoneNum());
        }
    }

    private void checkNewUser(String phoneNum) {
        Optional<User> user = userRepository.findByPhoneNum(phoneNum);
        if (user.isPresent()) {
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    private User findByPhoneNum(String phoneNum) {
        return userRepository.findByPhoneNum(phoneNum)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private void isCorrectCode(String phoneNum, String code) {
        String savedCode = redisService.getStrValue(VERIFICATION_PREFIX + phoneNum);
        if (!code.equals(savedCode)) {
            throw new UserException(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }
    }

    private void saveCode(String phoneNum, String verificationCode) {
        redisService.setValue(VERIFICATION_PREFIX + phoneNum, verificationCode,
            VERIFICATION_DURATION);
    }

    private String sendCode(String verificationCode, String phoneNum) {
        String message = MessageUtils.generateVerificationText(verificationCode);
        try {
            smsSender.sendMessage(phoneNum, message);
        } catch (Exception e) {
            throw new UserException(ErrorCode.SMS_SEND_ERROR);
        }
        return verificationCode;
    }
}
