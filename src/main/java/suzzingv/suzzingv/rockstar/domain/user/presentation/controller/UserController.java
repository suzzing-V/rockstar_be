package suzzingv.suzzingv.rockstar.domain.user.presentation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.user.application.service.AuthService;
import suzzingv.suzzingv.rockstar.domain.user.application.service.UserService;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.FcmRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.NicknameRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.*;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;
import suzzingv.suzzingv.rockstar.global.security.jwt.service.JwtService;

/**
 * [Controller] 사용자와 관련된 HTTP 요청을 처리하는 컨트롤러
 * 리팩토링을 통해 인증 관련 요청은 AuthService로, 사용자 정보 관련 요청은 UserService로 위임합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/user")
public class UserController {

    // 역할에 따라 분리된 서비스를 주입받습니다.
    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService; // JwtService는 토큰 추출 용도로만 사용됩니다.

    /**
     * [인증] SMS 인증 코드를 발송합니다.
     */
    @PostMapping("/verification-code")
    public ResponseEntity<VerificationCodeResponse> sendVerificationCode(
        @Valid @RequestBody PhoneNumRequest request) {
        // 책임이 AuthService로 이전되었습니다.
        VerificationCodeResponse response = authService.sendVerificationCode(request);
        return ResponseEntity.ok(response);
    }

    /**
     * [인증] 로그인 또는 회원가입을 처리합니다.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody CodeRequest request) {
        // 책임이 AuthService로 이전되었습니다.
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * [정보] FCM 토큰을 업데이트합니다.
     */
    @PatchMapping("/fcm")
    public ResponseEntity<UserUpdateResponse> updateFcmToken(@AuthenticationPrincipal User user, @RequestBody FcmRequest request) {
        // 이 책임은 여전히 UserService에 있습니다.
        UserUpdateResponse response = userService.updateFcmToken(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * [정보] 닉네임을 변경합니다.
     */
    @PatchMapping("/nickname")
    public ResponseEntity<UserUpdateResponse> updateNickname(@AuthenticationPrincipal User user,
        @RequestBody @Valid NicknameRequest request) {
        // 이 책임은 여전히 UserService에 있습니다.
        UserUpdateResponse response = userService.updateNickname(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * [정보] 내 정보를 조회합니다.
     */
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        // 이 책임은 여전히 UserService에 있습니다.
        UserInfoResponse response = userService.getUserInfo(user.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * [정보] 밴드 내 내 정보를 조회합니다.
     */
    @GetMapping("/band/{bandId}")
    public ResponseEntity<UserInfoByBandResponse> getUserInfoByBand(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        // 이 책임은 여전히 UserService에 있습니다.
        UserInfoByBandResponse response = userService.getUserInfoByBand(user.getId(), bandId);
        return ResponseEntity.ok(response);
    }

    /**
     * [정보] 밴드 멤버 목록을 조회합니다.
     */
    @GetMapping("/band-member/{bandId}")
    public ResponseEntity<Page<UserInfoByBandResponse>> getBandMembers(@PathVariable Long bandId, @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        // 이 책임은 여전히 UserService에 있습니다.
        Page<UserInfoByBandResponse> response = userService.getUsersByBand(bandId, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * [인증] 토큰을 재발급합니다.
     */
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(HttpServletRequest request) {
        // 책임이 AuthService로 이전되었습니다.
        String refreshToken = jwtService.extractRefreshToken(request);
        TokenResponse response = authService.reissueToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    /**
     * [인증] 로그아웃을 처리합니다.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // 책임이 AuthService로 이전되었습니다.
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new UserException(ErrorCode.ACCESS_TOKEN_REQUIRED));
        String refreshToken = jwtService.extractRefreshToken(request);
        authService.logout(accessToken, refreshToken);

        return ResponseEntity.ok().build();
    }

    /**
     * [정보] 회원 탈퇴를 처리합니다.
     */
    @DeleteMapping
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal User user, HttpServletRequest request) {
        // 회원 탈퇴는 두 가지 책임을 모두 포함합니다.
        // 1. 토큰 무효화 (인증 책임)
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new UserException(ErrorCode.ACCESS_TOKEN_REQUIRED));
        String refreshToken = jwtService.extractRefreshToken(request);
        authService.logout(accessToken, refreshToken);

        // 2. 사용자 데이터 처리 (정보 관리 책임)
        userService.withdraw(user.getId());

        return ResponseEntity.ok().build();
    }
}
