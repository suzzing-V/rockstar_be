package suzzingv.suzzingv.rockstar.domain.band.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.*;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res.*;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/band")
public class BandController {

    private final BandService bandService;

    @PostMapping
    public ResponseEntity<BandIdResponse> createBand(@AuthenticationPrincipal User user,
        @RequestBody BandRequest request) {
        BandIdResponse response = bandService.createBand(user.getId(), request);
        bandService.createBandUser(response.getBandId(), user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/invitationUrl")
    public ResponseEntity<BandNameResponse> findByInvitationUrl(@RequestBody BandInvitationUrlRequest request) {
        BandNameResponse response = bandService.findBandByInvitationUrl(request.getInvitationUrl());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/entry/{bandId}")
    public ResponseEntity<BandNameResponse> applyForEntry(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        BandNameResponse response = bandService.applyForEntry(user.getId(), bandId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entry/{bandId}")
    public ResponseEntity<List<EntryApplicationResponse>> getEntryApplicationList(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        List<EntryApplicationResponse> response = bandService.getEntryApplicationList(user.getId(), bandId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/entry/accept")
    public ResponseEntity<EntryAcceptResponse> acceptEntry(@AuthenticationPrincipal User user, @RequestBody EntryAcceptRequest request) {
        EntryAcceptResponse response = bandService.acceptEntry(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/entry")
    public ResponseEntity<Void> deleteEntry(@AuthenticationPrincipal User user, @RequestBody EntryAcceptRequest request) {
        bandService.deleteEntry(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<Page<BandShortInfoResponse>> getListByUser(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<BandShortInfoResponse> response = bandService.getListByUser(user.getId(), page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/url/{bandId}")
    public ResponseEntity<BandUrlResponse> getListByUser(@PathVariable Long bandId) {
        BandUrlResponse response = bandService.getInvitationUrl(bandId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/name")
    public ResponseEntity<BandIdResponse> getListByUser(@RequestBody BandNameRequest request) {
        BandIdResponse response = bandService.updateBandName(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bandId}")
    public ResponseEntity<BandResponse> getBandInfo(@PathVariable Long bandId) {
        BandResponse response = bandService.getBandInfo(bandId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{bandId}")
    public ResponseEntity<Void> withdrawBand(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        bandService.withdrawBand(user.getId(), bandId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bandId}")
    public ResponseEntity<Void> deleteBand(@AuthenticationPrincipal User user, @PathVariable Long bandId) {
        bandService.deleteBand(bandId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/manager")
    public ResponseEntity<BandIdResponse> deleteBand(@RequestBody BandManagerRequest request) {
        BandIdResponse response = bandService.updateBandManager(request);
        return ResponseEntity.ok(response);
    }
}
