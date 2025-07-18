package suzzingv.suzzingv.rockstar.domain.user.presentation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.user.application.service.UserService;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.NicknameRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.suzzingv.rockstar.domain.user.presentation.dto.res.*;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;
import suzzingv.suzzingv.rockstar.global.security.jwt.service.JwtService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/verification-code")
    public ResponseEntity<VerificationCodeResponse> sendVerificationCode(
        @Valid @RequestBody PhoneNumRequest request) {
        VerificationCodeResponse response = userService.sendVerificationCode(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody CodeRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<UserUpdateResponse> updateNickname(@AuthenticationPrincipal User user,
        @RequestBody @Valid NicknameRequest request) {
        UserUpdateResponse response = userService.updateNickname(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        UserInfoResponse response = userService.getUserInfo(user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/band/{bandId}")
    public ResponseEntity<UserInfoByBandResponse> getUserInfoByBand(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        UserInfoByBandResponse response = userService.getUserInfoByBand(user.getId(), bandId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/band-member/{bandId}")
    public ResponseEntity<List<UserInfoByBandResponse>> getBandMembers(@PathVariable Long bandId) {
        List<UserInfoByBandResponse> response = userService.getUsersByBand(bandId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(HttpServletRequest request, @AuthenticationPrincipal User user) {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        TokenResponse response = userService.reissueToken(refreshToken);

        return ResponseEntity.ok(response);
    }
}
