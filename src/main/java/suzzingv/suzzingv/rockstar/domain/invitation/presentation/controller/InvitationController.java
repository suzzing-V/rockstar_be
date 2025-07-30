package suzzingv.suzzingv.rockstar.domain.invitation.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.EntryAcceptRequest;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res.EntryAcceptResponse;
import suzzingv.suzzingv.rockstar.domain.invitation.application.service.InvitationService;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.InvitationRequest;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/invite")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<InvitationResponse> invite(@RequestBody InvitationRequest request) {
        InvitationResponse response = invitationService.invite(request);
        return ResponseEntity.ok(response);
    }
}