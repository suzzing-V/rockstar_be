package suzzingv.suzzingv.rockstar.domain.availability.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import suzzingv.suzzingv.rockstar.domain.availability.application.service.AvailabilityService;
import suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.req.UnavailabilityRequest;
import suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.res.UnavailabilityResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<UnavailabilityResponse> createUnAvailableDay(@AuthenticationPrincipal User user, @RequestBody UnavailabilityRequest request) {
        UnavailabilityResponse response = availabilityService.createUnAvailableDay(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
