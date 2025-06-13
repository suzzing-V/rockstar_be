package suzzingv.suzzingv.rtr.domain.band.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandResponse;
import suzzingv.suzzingv.rtr.domain.band.application.service.BandService;
import suzzingv.suzzingv.rtr.domain.user.domain.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/band")
public class BandController {

    private final BandService bandService;

    @PostMapping
    public ResponseEntity<BandResponse> createBand(@AuthenticationPrincipal User user, @RequestBody BandRequest request) {
        BandResponse response = bandService.createBand(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
