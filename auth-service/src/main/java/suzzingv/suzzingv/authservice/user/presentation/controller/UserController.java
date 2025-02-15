package suzzingv.suzzingv.authservice.user.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.authservice.user.application.service.UserService;
import suzzingv.suzzingv.authservice.user.domain.entity.User;
import suzzingv.suzzingv.authservice.user.presentation.dto.req.NicknameRequest;
import suzzingv.suzzingv.authservice.user.presentation.dto.req.CodeRequest;
import suzzingv.suzzingv.authservice.user.presentation.dto.res.LoginResponse;
import suzzingv.suzzingv.authservice.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.suzzingv.authservice.user.presentation.dto.res.UserUpdateResponse;
import suzzingv.suzzingv.authservice.user.presentation.dto.res.VerificationCodeResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/verification-code")
    public ResponseEntity<VerificationCodeResponse> sendVerificationCode(@Valid @RequestBody PhoneNumRequest request) {
        VerificationCodeResponse response = userService.sendVerificationCode(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody CodeRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<UserUpdateResponse> updateNickname(@AuthenticationPrincipal User user, @RequestBody @Valid NicknameRequest request) {
        UserUpdateResponse response = userService.updateNickname(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
