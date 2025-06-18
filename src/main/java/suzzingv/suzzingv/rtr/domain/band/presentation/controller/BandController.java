package suzzingv.suzzingv.rtr.domain.band.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandUrlRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandResponse;
import suzzingv.suzzingv.rtr.domain.band.application.service.BandService;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandIdResponse;
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

    @GetMapping("/url")
    public ResponseEntity<BandIdResponse> findByUrl(@RequestBody BandUrlRequest request) {
        BandIdResponse response = bandService.getBandUrl(request.getUrl());
        return ResponseEntity.ok(response);
    }
}
