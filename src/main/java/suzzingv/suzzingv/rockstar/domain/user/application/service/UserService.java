package suzzingv.suzzingv.rockstar.domain.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.BandUser;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandUserRepository;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.UserFcm;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserFcmRepository;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.FcmRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.NicknameRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.UserInfoByBandResponse;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.UserInfoResponse;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.UserUpdateResponse;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

import java.time.LocalDateTime;

/**
 * [Service] 사용자 정보 관리 로직을 담당하는 서비스
 * SRP(단일 책임 원칙)에 따라 인증 관련 기능은 AuthService로 분리되었습니다.
 * 이 서비스는 사용자 정보 조회, 수정, 탈퇴 등의 책임을 가집니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    // AuthService로 책임이 이전된 의존성(SmsSender, RedisService, JwtService)은 제거되었습니다.
    private final UserRepository userRepository;
    private final BandService bandService;
    private final BandUserRepository bandUserRepository;
    private final UserFcmRepository  userFcmRepository;

    /**
     * 사용자의 닉네임을 변경합니다.
     * @param userId 사용자 ID
     * @param request 변경할 닉네임
     * @return 업데이트된 사용자 정보
     */
    public UserUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        // 닉네임 중복 검사 로직은 그대로 유지합니다.
        checkNicknameDuplication(request);
        User user = findUserById(userId);
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

    /**
     * ID로 사용자를 조회합니다.
     * @param userId 사용자 ID
     * @return 조회된 User 엔티티
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 사용자 정보를 조회합니다.
     * @param userId 사용자 ID
     * @return 사용자 정보 DTO
     */
    public UserInfoResponse getUserInfo(Long userId) {
        User user = findUserById(userId);
        return UserInfoResponse.from(user);
    }

    /**
     * 특정 밴드 내에서의 사용자 정보를 조회합니다.
     * @param userId 사용자 ID
     * @param bandId 밴드 ID
     * @return 밴드 내 사용자 정보 DTO
     */
    public UserInfoByBandResponse getUserInfoByBand(Long userId, Long bandId) {
        Band band = bandService.findById(bandId);
        User user = findUserById(userId);
        boolean isManager = band.getManagerId().equals(userId);

        return UserInfoByBandResponse.of(user, isManager);
    }

    /**
     * 특정 밴드에 속한 사용자 목록을 페이징하여 조회합니다.
     * @param bandId 밴드 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 밴드 내 사용자 정보
     */
    public Page<UserInfoByBandResponse> getUsersByBand(Long bandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "userId"));
        Page<BandUser> bandUsers = bandUserRepository.findByBandId(bandId, pageable);
        Band band = bandService.findById(bandId);

        return bandUsers.map(bandUser -> {
            User user = findUserById(bandUser.getUserId());
            boolean isManager = band.getManagerId().equals(user.getId());
            return UserInfoByBandResponse.of(user, isManager);
        });
    }

    /**
     * 회원 탈퇴를 처리합니다. (Soft Delete)
     * @param userId 탈퇴할 사용자 ID
     */
    public void withdraw(Long userId) {
        // TODO: 일정 기간 지나면 배치로 하드 삭제
        User user = findUserById(userId);
        user.changeDeletedAt(LocalDateTime.now());

        // 사용자가 속한 밴드 정보 및 초대 정보 등을 정리합니다.
        bandUserRepository.deleteByUserId(userId);
        bandService.delegateManagerOfUserId(userId);
        bandService.deleteEntryByUserId(userId);
    }

    /**
     * FCM 토큰을 업데이트합니다.
     * @param userId 사용자 ID
     * @param request 새로운 FCM 토큰
     * @return 업데이트된 사용자 정보
     */
    public UserUpdateResponse updateFcmToken(Long userId, FcmRequest request) {
        UserFcm userFcm = getUserFcm(userId);
        userFcm.changeFcmToken(request.getFcmToken());

        return UserUpdateResponse.builder()
                .userId(userId)
                .build();
    }

    public UserFcm getUserFcm(Long userId) {
        return userFcmRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_FCM_NOT_FOUND));
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickName(nickname)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }
}