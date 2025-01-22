package suzzingv.rtr.ruletherockbe.domain.user.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import suzzingv.rtr.ruletherockbe.domain.user.application.service.UserService;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.CodeRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.LoginResponse;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.req.PhoneNumRequest;
import suzzingv.rtr.ruletherockbe.domain.user.presentation.dto.res.VerificationCodeResponse;

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
}
