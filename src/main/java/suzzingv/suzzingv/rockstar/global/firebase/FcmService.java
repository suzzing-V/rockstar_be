package suzzingv.suzzingv.rockstar.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

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
}
