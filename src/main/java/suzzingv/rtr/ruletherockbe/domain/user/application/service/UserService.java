package suzzingv.rtr.ruletherockbe.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import suzzingv.rtr.ruletherockbe.domain.user.exception.UserException;
import suzzingv.rtr.ruletherockbe.domain.user.infrastructure.UserRepository;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.VerificationCodeResponse;
import suzzingv.rtr.ruletherockbe.global.redis.RedisService;
import suzzingv.rtr.ruletherockbe.global.response.properties.ErrorCode;
import suzzingv.rtr.ruletherockbe.global.sms.MessageUtils;
import suzzingv.rtr.ruletherockbe.global.sms.SmsSender;

import java.time.Duration;

import static suzzingv.rtr.ruletherockbe.domain.user.util.VerificationCodeGenerator.getCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SmsSender smsSender;
    private final RedisService redisService;

    private static final String VERIFICATION_PREFIX = "verify_";
    private static final Duration VERIFICATION_DURATION = Duration.ofMinutes(5);

    public VerificationCodeResponse sendVerificationCode(PhoneNumRequest request) {
        String verificationCode = getCode();
//        sendCode(verificationCode, request.getPhoneNum());
        saveCode(request, verificationCode);
        return VerificationCodeResponse.builder()
                .code(verificationCode)
                .build();
    }

    private void saveCode(PhoneNumRequest request, String verificationCode) {
        redisService.setValue(VERIFICATION_PREFIX + verificationCode, request.getPhoneNum(), VERIFICATION_DURATION);
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
