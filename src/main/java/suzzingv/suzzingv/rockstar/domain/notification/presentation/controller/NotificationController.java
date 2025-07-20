package suzzingv.suzzingv.rockstar.domain.notification.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.notification.application.NotificationService;
import suzzingv.suzzingv.rockstar.domain.notification.presentation.dto.res.NotificationUserIdResponse;
import suzzingv.suzzingv.rockstar.domain.notification.presentation.dto.res.NotificationResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsOfUser(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "20") int size) {
        Page<NotificationResponse> responses = notificationService.getNotificationsByUser(user.getId(), page, size);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/read/${notificationId}")
    public ResponseEntity<NotificationUserIdResponse> read(@PathVariable Long notificationId) {
        NotificationUserIdResponse response = notificationService.read(notificationId);
        return ResponseEntity.ok(response);
    }
}
