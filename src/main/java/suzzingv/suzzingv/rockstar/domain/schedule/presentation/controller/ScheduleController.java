package suzzingv.suzzingv.rockstar.domain.schedule.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.schedule.appplication.ScheduleService;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@RestController
@RequestMapping("/api/v0/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/band/{bandId}")
    public ResponseEntity<Page<ScheduleResponse>> getScheduleByBand(@PathVariable Long bandId, @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<ScheduleResponse> response = scheduleService.getByBand(bandId, page, size);
        return ResponseEntity.ok(response);
    }
}
