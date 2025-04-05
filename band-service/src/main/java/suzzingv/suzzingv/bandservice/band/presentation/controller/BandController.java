package suzzingv.suzzingv.bandservice.band.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.bandservice.band.application.service.BandService;
import suzzingv.suzzingv.bandservice.band.presentation.dto.res.BandResponse;
import suzzingv.suzzingv.bandservice.band.presentation.dto.req.BandRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/band")
public class BandController {

    private final BandService bandService;

    @PostMapping
    public ResponseEntity<BandResponse> createBand(@RequestHeader(value = "X-User-ID", required = false) Long userId, @RequestBody BandRequest request) {
        BandResponse response = bandService.createBand(userId, request);
        return ResponseEntity.ok(response);
    }
}
