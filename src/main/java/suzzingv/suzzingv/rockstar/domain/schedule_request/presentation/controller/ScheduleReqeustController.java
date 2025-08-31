package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.schedule_request.application.ScheduleRequestService;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.req.ScheduleRequestRequest;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ScheduleRequestIdResponse;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ScheduleRequestResponse;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ShortScheduleRequestResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

import java.util.List;

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

    @GetMapping("/{requestId}")
    public ResponseEntity<ScheduleRequestResponse> getScheduleRequestInfo(@PathVariable Long requestId) {
        ScheduleRequestResponse response = scheduleRequestService.getScheduleRequestInfo(requestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/band/{bandId}")
    public ResponseEntity<Page<ShortScheduleRequestResponse>> getScheduleRequestsOfBand(@AuthenticationPrincipal User user, @PathVariable Long bandId, @RequestParam(defaultValue = "0") int page,
                                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ShortScheduleRequestResponse> responses = scheduleRequestService.getScheduleRequestsOfBand(user.getId(), bandId, page, size);
        return ResponseEntity.ok(responses);
    }
}
