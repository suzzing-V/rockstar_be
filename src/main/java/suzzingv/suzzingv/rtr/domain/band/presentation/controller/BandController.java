package suzzingv.suzzingv.rtr.domain.band.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import suzzingv.suzzingv.rtr.domain.band.application.service.BandService;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandInvitationUrlRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandNameResponse;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandResponse;
import suzzingv.suzzingv.rtr.domain.user.domain.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/band")
public class BandController {

    private final BandService bandService;

    @PostMapping
    public ResponseEntity<BandResponse> createBand(@AuthenticationPrincipal User user,
        @RequestBody BandRequest request) {
        BandResponse response = bandService.createBand(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/invitationUrl")
    public ResponseEntity<BandNameResponse> findByInvitationUrl(@RequestBody BandInvitationUrlRequest request) {
        BandNameResponse response = bandService.getInvitationUrl(request.getInvitationUrl());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join/{bandId}")
    public ResponseEntity<BandNameResponse> join(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        BandNameResponse response = bandService.join(user.getId(), bandId);
        return ResponseEntity.ok(response);
    }
}
