package suzzingv.suzzingv.rockstar.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequest;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.UserFcm;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserFcmRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    private final UserFcmRepository userFcmRepository;

    @Async
    public void sendScheduleInfoPush(String fcmToken, String title, String body, Long bandId, Long scheduleId) {
        if (fcmToken == null || fcmToken.isEmpty()) return;

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("type", "SCHEDULE_INFO")
                .putData("scheduleId", String.valueOf(scheduleId)) // ✅ 예시 ID
                .putData("bandId", String.valueOf(bandId))
                .putData("timestamp", String.valueOf(System.currentTimeMillis()))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ 푸시 전송 완료: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ 푸시 전송 실패: " + e.getMessage());
        }
    }

    @Async
    public void sendScheduleListPush(String fcmToken, String title, String body, Long bandId) {
        if (fcmToken == null || fcmToken.isEmpty()) return;

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("type", "SCHEDULE_LIST")
                .putData("bandId", String.valueOf(bandId))
                .putData("timestamp", String.valueOf(System.currentTimeMillis()))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ 푸시 전송 완료: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ 푸시 전송 실패: " + e.getMessage());
        }
    }

    @Async
    public void sendInvitationPush(String title, Band band, Long userId) {
        UserFcm userFcm = userFcmRepository.findByUserId(userId).orElse(null);

        if (userFcm == null || userFcm.getFcmToken() == null) {
            log.warn("UserFcm not found for userId: " + userId);
            return;
        }

        String fcmToken = userFcm.getFcmToken();
        String body = band.getName() + " 밴드에서 초대를 보냈습니다.";
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("type", "INVITATION")
                .putData("userId", String.valueOf(userId))
                .putData("timestamp", String.valueOf(System.currentTimeMillis()))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ 푸시 전송 완료: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ 푸시 전송 실패: " + e.getMessage());
        }
    }

    @Async
    public void sendInvitationAcceptPush(User user, Band band) {
        UserFcm userFcm = userFcmRepository.findByUserId(band.getManagerId()).orElse(null);

        if (userFcm == null || userFcm.getFcmToken() == null) {
            log.warn("UserFcm not found for userId: " + user.getId());
            return;
        }

        String fcmToken = userFcm.getFcmToken();
        String body = user.getNickName() + "님이 초대를 수락했습니다.";
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(band.getName())
                        .setBody(body)
                        .build())
                .putData("type", "INVITATION_ACCEPT")
                .putData("bandId", String.valueOf(band.getId()))
                .putData("timestamp", String.valueOf(System.currentTimeMillis()))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ 푸시 전송 완료: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ 푸시 전송 실패: " + e.getMessage());
        }
    }

    @Async
    public void sendScheduleRequestPush(Long userId, Band band, ScheduleRequest scheduleRequest) {
        UserFcm userFcm = userFcmRepository.findByUserId(userId).orElse(null);

        if (userFcm == null || userFcm.getFcmToken() == null) {
            log.warn("UserFcm not found for userId: " + userId);
            return;
        }

        String fcmToken = userFcm.getFcmToken();
        String body = "일정을 업데이트 해주세요.";
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(band.getName())
                        .setBody(body)
                        .build())
                .putData("type", "SCHEDULE_REQUEST")
                .putData("scheduleRequestId", String.valueOf(scheduleRequest.getId()))
                .putData("timestamp", String.valueOf(System.currentTimeMillis()))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ 푸시 전송 완료: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ 푸시 전송 실패: " + e.getMessage());
        }
    }
}
