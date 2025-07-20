package suzzingv.suzzingv.rockstar.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandUserRepository;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.Notification;
import suzzingv.suzzingv.rockstar.domain.notification.domain.entity.NotificationUser;
import suzzingv.suzzingv.rockstar.domain.notification.domain.enums.NotificationType;
import suzzingv.suzzingv.rockstar.domain.notification.exception.NotificationException;
import suzzingv.suzzingv.rockstar.domain.notification.infrastructure.NotificationRepository;
import suzzingv.suzzingv.rockstar.domain.notification.infrastructure.NotificationUserRepository;
import suzzingv.suzzingv.rockstar.domain.notification.presentation.dto.res.NotificationResponse;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BandUserRepository bandUserRepository;
    private final NotificationUserRepository notificationUserRepository;


    public void createScheduleCreationNotification(Band band, Long scheduleId, LocalDateTime startDateTime) {
        String content = startDateTime.getMonthValue() + "월 " + startDateTime.getDayOfMonth() + "일에 일정이 생성되었습니다.";

        Notification notification = Notification.builder()
                .title(band.getName())
                .content(content)
                .contentId(scheduleId)
                .notificationType(NotificationType.SCHEDULE_CREATED)
                .build();
        notificationRepository.save(notification);

        createNotificationOfMembers(band, notification);
    }

    private void createNotificationOfMembers(Band band, Notification notification) {
        bandUserRepository.findByBandId(band.getId())
                .forEach(bandUser -> {
                    NotificationUser notificationUser = NotificationUser.builder()
                            .notificationId(notification.getId())
                            .userId(bandUser.getUserId())
                            .build();
                    notificationUserRepository.save(notificationUser);
                    // TODO: 푸시알림 보내기
        });
    }

    public void createScheduleUpdateNotification(Band band, Long scheduleId, LocalDateTime oldDateTime) {
        String content = oldDateTime.getMonthValue() + "월 " + oldDateTime.getDayOfMonth() + "일의 일정이 수정되었습니다.";

        Notification notification = Notification.builder()
                .title(band.getName())
                .content(content)
                .contentId(scheduleId)
                .notificationType(NotificationType.SCHEDULE_UPDATED)
                .build();
        notificationRepository.save(notification);

        createNotificationOfMembers(band, notification);
    }

    public void createScheduleDeleteNotification(Band band, Long scheduleId, LocalDateTime startDate) {
        String content = startDate.getMonthValue() + "월 " + startDate.getDayOfMonth() + "일의 일정이 삭제되었습니다.";

        Notification notification = Notification.builder()
                .title(band.getName())
                .content(content)
                .contentId(scheduleId)
                .notificationType(NotificationType.SCHEDULE_DELETED)
                .build();
        notificationRepository.save(notification);

        createNotificationOfMembers(band, notification);
    }

    public void createEntryApplyNotification(Band band, User user) {
        String content = user.getNickName() + "님이 가입을 신청했습니다.";

        Notification notification = Notification.builder()
                .title(band.getName())
                .content(content)
                .contentId(user.getId())
                .notificationType(NotificationType.ENTRY_APPLICATION)
                .build();
        notificationRepository.save(notification);

        NotificationUser notificationUser = NotificationUser.builder()
                .notificationId(notification.getId())
                .userId(band.getManagerId())
                .build();
        notificationUserRepository.save(notificationUser);
        // TODO: 푸시알림 보내기
    }

    public void createEntryAcceptNotification(Band band, User user) {
        String content = "가입 신청이 수락되었습니다.";

        Notification notification = Notification.builder()
                .title(band.getName())
                .content(content)
                .contentId(band.getId())
                .notificationType(NotificationType.ENTRY_ACCEPTED)
                .build();
        notificationRepository.save(notification);

        NotificationUser notificationUser = NotificationUser.builder()
                .notificationId(notification.getId())
                .userId(user.getId())
                .build();
        notificationUserRepository.save(notificationUser);
        // TODO: 푸시알림 보내기
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByUser(Long userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<NotificationResponse> respnoses = notificationUserRepository.findByUserId(userId, pageable)
                .map(notificationUser -> {
                    Notification notification = findById(notificationUser.getNotificationId());

                    return NotificationResponse.of(notification, notificationUser.getIsRead());
                });

        return respnoses;
    }

    private Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }
}
