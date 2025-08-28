package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.schedule_request.application.ScheduleRequestService;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.req.ScheduleRequestRequest;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ScheduleRequestIdResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@RestController
@RequestMapping("/api/v1/schedule-request")
@RequiredArgsConstructor
public class ScheduleReqeustController {

    private final ScheduleRequestService scheduleRequestService;

    @PostMapping
    public ResponseEntity<ScheduleRequestIdResponse> createScheduleRequest(@RequestBody ScheduleRequestRequest request) {
        ScheduleRequestIdResponse response = scheduleRequestService.createScheduleRequest(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/completion/{requestId}")
    public ResponseEntity<ScheduleRequestIdResponse> complete(@AuthenticationPrincipal User user, @PathVariable Long requestId) {
        ScheduleRequestIdResponse response = scheduleRequestService.completeRequest(user.getId(), requestId);
        return ResponseEntity.ok(response);
    }
}
