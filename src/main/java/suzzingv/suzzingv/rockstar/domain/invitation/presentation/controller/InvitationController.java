package suzzingv.suzzingv.rockstar.domain.invitation.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.invitation.application.service.InvitationService;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.InvitationRequest;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationInfoResponse;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationResponse;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationUserInfoResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

import java.util.List;

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

    @GetMapping("/list")
    public ResponseEntity<List<InvitationInfoResponse>> getInvitationList(@AuthenticationPrincipal User user) {
        List<InvitationInfoResponse> response = invitationService.getInvitationList(user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept/{bandId}")
    public ResponseEntity<InvitationResponse> acceptInvitation(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        InvitationResponse response = invitationService.acceptInvitation(user.getId(), bandId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject/{bandId}")
    public ResponseEntity<InvitationResponse> rejectInvitation(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        InvitationResponse response = invitationService.rejectInvitation(user.getId(), bandId);
        return ResponseEntity.ok(response);
    }
}