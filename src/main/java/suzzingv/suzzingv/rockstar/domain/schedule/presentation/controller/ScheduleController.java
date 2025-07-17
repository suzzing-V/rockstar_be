package suzzingv.suzzingv.rockstar.domain.schedule.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.schedule.appplication.ScheduleService;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.req.ScheduleRequest;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleIdResponse;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleListResponse;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@RestController
@RequestMapping("/api/v0/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/band/{bandId}")
    public ResponseEntity<ScheduleListResponse> getScheduleByBand(@AuthenticationPrincipal User user, @PathVariable Long bandId, @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        ScheduleListResponse response = scheduleService.getByBand(user.getId(), bandId, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ScheduleIdResponse> createSchedule(@AuthenticationPrincipal User user, @RequestBody ScheduleRequest request) {
        log.info(request.getDescription());
        ScheduleIdResponse response = scheduleService.createSchedule(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleIdResponse> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequest scheduleRequest) {
        ScheduleIdResponse response = scheduleService.updateSchedule(scheduleId, scheduleRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getSchedule(@AuthenticationPrincipal User user, @PathVariable Long scheduleId) {
        ScheduleResponse response = scheduleService.getSchedule(scheduleId);
        return ResponseEntity.ok(response);
    }

}
