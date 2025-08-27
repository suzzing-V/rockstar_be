package suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import suzzingv.suzzingv.rockstar.domain.schedule_request.application.ScheduleRequestService;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.req.ScheduleRequestRequest;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ScheduleRequestIdResponse;

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
}
