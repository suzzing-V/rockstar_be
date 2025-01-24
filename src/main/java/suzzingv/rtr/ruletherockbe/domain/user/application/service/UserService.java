package suzzingv.rtr.ruletherockbe.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import suzzingv.rtr.ruletherockbe.domain.user.domain.entity.User;
import suzzingv.rtr.ruletherockbe.domain.user.domain.enums.Role;
import suzzingv.rtr.ruletherockbe.domain.user.exception.UserException;
import suzzingv.rtr.ruletherockbe.domain.user.infrastructure.UserRepository;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.NicknameRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.LoginResponse;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.UserUpdateResponse;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.VerificationCodeResponse;
import suzzingv.rtr.ruletherockbe.global.redis.RedisService;
import suzzingv.rtr.ruletherockbe.global.response.properties.ErrorCode;
import suzzingv.rtr.ruletherockbe.global.security.jwt.service.JwtService;
import suzzingv.rtr.ruletherockbe.global.sms.MessageUtils;
import suzzingv.rtr.ruletherockbe.global.sms.SmsSender;

import java.time.Duration;
import java.util.Optional;

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
    checkUserByNew(request);
    String verificationCode = getCode();
        sendCode(verificationCode, request.getPhoneNum());
        saveCode(request.getPhoneNum(), verificationCode);
        return VerificationCodeResponse.builder()
                .code(verificationCode)
                .build();
    }

    public LoginResponse login(CodeRequest request) {
        isCorrectCode(request.getPhoneNum(), request.getCode());
        String accessToken = jwtService.createAccessToken(request.getPhoneNum());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, request.getPhoneNum());
        User user = getUserByIsNew(request);

        return LoginResponse.of(accessToken, refreshToken, user.getId());
    }

    public UserUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        checkNicknameDuplication(request);
        log.info("중복확인");
        User user = findUserById(userId);
        user.changeNickname(request.getNickname());
        return UserUpdateResponse.builder()
                .userId(user.getId())
                .build();
    }

    private void checkNicknameDuplication(NicknameRequest request) {
        userRepository.findByNickName(request.getNickname())
                .ifPresent(user -> { throw new UserException(ErrorCode.NICKNAME_DUPLICATION);} );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private User getUserByIsNew(CodeRequest request) {
        if(request.getIsNew()) {
            User user = User.builder()
                    .phoneNum(request.getPhoneNum())
                    .role(Role.USER)
                    .build();
            return userRepository.save(user);
        } else {
            return getByPhoneNum(request);
        }
    }

    private User getByPhoneNum(CodeRequest request) {
        return findByPhoneNum(request.getPhoneNum());
    }

    private User findByPhoneNum(String phoneNum) {
        return userRepository.findByPhoneNum(phoneNum)
                        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkNewUser(String phoneNum) {
        Optional<User> user = userRepository.findByPhoneNum(phoneNum);
        if(user.isPresent()) {
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    private void checkUserByNew(PhoneNumRequest request) {
        if(request.getIsNew()) {
            checkNewUser(request.getPhoneNum());
        } else {
            findByPhoneNum(request.getPhoneNum());
        }
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
