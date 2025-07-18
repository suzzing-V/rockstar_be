package suzzingv.suzzingv.rockstar.domain.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.BandUser;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandUserRepository;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.domain.enums.Role;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.NicknameRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.*;
import suzzingv.suzzingv.rockstar.domain.user.util.VerificationCodeGenerator;
import suzzingv.suzzingv.rockstar.global.redis.RedisService;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;
import suzzingv.suzzingv.rockstar.global.security.jwt.service.JwtService;
import suzzingv.suzzingv.rockstar.global.sms.MessageUtils;
import suzzingv.suzzingv.rockstar.global.sms.SmsSender;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BandService bandService;
    private final SmsSender smsSender;
    private final RedisService redisService;
    private final JwtService jwtService;
    private final BandUserRepository bandUserRepository;

    private static final String VERIFICATION_PREFIX = "verify_";
    private static final Duration VERIFICATION_DURATION = Duration.ofMinutes(1);

    public VerificationCodeResponse sendVerificationCode(PhoneNumRequest request) {
        checkUserByNew(request);
        String verificationCode = VerificationCodeGenerator.getCode();
//        sendCode(verificationCode, request.getPhoneNum());
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

        return LoginResponse.of(accessToken, refreshToken, user.getId(), user.getNickName());
    }

    public UserUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        checkNicknameDuplication(request);
        log.info("중복확인");
        User user = findUserById(userId);
        log.info(user.getPhoneNum());
        user.changeNickname(request.getNickname());
        return UserUpdateResponse.builder()
            .userId(user.getId())
            .build();
    }

    private void checkNicknameDuplication(NicknameRequest request) {
        userRepository.findByNickName(request.getNickname())
            .ifPresent(user -> {
                throw new UserException(ErrorCode.NICKNAME_DUPLICATION);
            });
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private User getUserByIsNew(CodeRequest request) {
        if (request.getIsNew()) {
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
        if (user.isPresent()) {
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    private void checkUserByNew(PhoneNumRequest request) {
        if (request.getIsNew()) {
            checkNewUser(request.getPhoneNum());
        } else {
            findByPhoneNum(request.getPhoneNum());
        }
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

    public UserInfoResponse getUserInfo(Long userId) {
        User user = findUserById(userId);

        return UserInfoResponse.from(user);
    }

    public TokenResponse reissueToken(String refreshToken) {
        String phoneNum = jwtService.checkRefreshToken(refreshToken);

        String newAccessToken = jwtService.createAccessToken(phoneNum);
        String newRefreshToken = jwtService.reissueRefreshToken(refreshToken, phoneNum);

        return TokenResponse.of(newAccessToken, newRefreshToken);
        // accessToken 만료 -> refreshToken 보내 토큰 재발급 -> refreshToken도 만료됐으면 다시 로그인해야됨
    }

    public UserInfoByBandResponse getUserInfoByBand(Long userId, Long bandId) {
        Band band = bandService.findById(bandId);
        User user = findUserById(userId);
        boolean isManager = band.getManagerId().equals(userId);

        return UserInfoByBandResponse.of(user, isManager);
    }

    public List<UserInfoByBandResponse> getUsersByBand(Long bandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        List<BandUser> bandUsers = bandUserRepository.findByBandId(bandId, pageable).toList();
        Band band = bandService.findById(bandId);

        return bandUsers.stream().map(bandUser -> {
            User user = findUserById(bandUser.getUserId());
            boolean isManager = band.getManagerId().equals(user.getId());
            return UserInfoByBandResponse.of(user, isManager);
        }).toList();
    }
}
