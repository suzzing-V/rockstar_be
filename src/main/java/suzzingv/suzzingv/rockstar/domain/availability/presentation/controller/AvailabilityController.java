package suzzingv.suzzingv.rockstar.domain.availability.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.availability.application.service.AvailabilityService;
import suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.req.UnavailabilityRequest;
import suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.res.DayUnavailabilityResponse;
import suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.res.UnavailabilityResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<DayUnavailabilityResponse>> getUnavailableDaysByMonth(
            @AuthenticationPrincipal User user,
            @RequestParam int year,
            @RequestParam int month) {
        List<DayUnavailabilityResponse> responses = availabilityService.getUnavailableDaysByMonth(user.getId(), year, month);
        return ResponseEntity.ok(responses);
    }
}
