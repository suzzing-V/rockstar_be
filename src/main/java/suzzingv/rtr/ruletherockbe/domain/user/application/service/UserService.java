package suzzingv.rtr.ruletherockbe.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import suzzingv.rtr.ruletherockbe.domain.user.domain.entity.User;
import suzzingv.rtr.ruletherockbe.domain.user.exception.UserException;
import suzzingv.rtr.ruletherockbe.domain.user.infrastructure.UserRepository;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.LoginResponse;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.VerificationCodeResponse;
import suzzingv.rtr.ruletherockbe.global.redis.RedisService;
import suzzingv.rtr.ruletherockbe.global.response.properties.ErrorCode;
import suzzingv.rtr.ruletherockbe.global.security.jwt.service.JwtService;
import suzzingv.rtr.ruletherockbe.global.sms.MessageUtils;
import suzzingv.rtr.ruletherockbe.global.sms.SmsSender;

import java.time.Duration;

import static suzzingv.rtr.ruletherockbe.domain.user.util.VerificationCodeGenerator.getCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SmsSender smsSender;
    private final RedisService redisService;
    private final JwtService jwtService;

    private static final String VERIFICATION_PREFIX = "verify_";
    private static final Duration VERIFICATION_DURATION = Duration.ofMinutes(1);

    public VerificationCodeResponse sendVerificationCode(PhoneNumRequest request) {
        String verificationCode = getCode();
//        sendCode(verificationCode, request.getPhoneNum());
        saveCode(request.getPhoneNum(), verificationCode);
        return VerificationCodeResponse.builder()
                .code(verificationCode)
                .build();
    }

    public LoginResponse login(CodeRequest request) {
        User user = getByPhoneNum(request);
        isCorrectCode(request.getPhoneNum(), request.getCode());
        String accessToken = jwtService.createAccessToken(request.getPhoneNum());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, request.getPhoneNum());

        return LoginResponse.of(accessToken, refreshToken, user.getId());
    }

    private User getByPhoneNum(CodeRequest request) {
        return findByPhoneNum(request.getPhoneNum());
    }

    private User findByPhoneNum(String phoneNum) {
        return userRepository.findByPhoneNum(phoneNum)
                        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private void isCorrectCode(String phoneNum, String code) {
        String savedCode = redisService.getStrValue(VERIFICATION_PREFIX + phoneNum);
        if(!code.equals(savedCode)) {
            throw new UserException(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }
    }

    private void saveCode(String phoneNum, String verificationCode) {
        redisService.setValue(VERIFICATION_PREFIX + phoneNum, verificationCode, VERIFICATION_DURATION);
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
