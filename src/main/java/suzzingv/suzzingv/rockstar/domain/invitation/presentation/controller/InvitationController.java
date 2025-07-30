package suzzingv.suzzingv.rockstar.domain.invitation.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.invitation.application.service.InvitationService;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.InvitationRequest;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationResponse;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationUserInfoResponse;

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

    @GetMapping("/search/{bandId}/{nickname}")
    public ResponseEntity<InvitationUserInfoResponse> findMemberByNickname(@PathVariable String nickname, @PathVariable Long bandId) {
        InvitationUserInfoResponse response = invitationService.searchUser(bandId, nickname);
        return ResponseEntity.ok(response);
    }
}