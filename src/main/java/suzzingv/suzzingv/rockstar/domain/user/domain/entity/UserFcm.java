package suzzingv.suzzingv.rockstar.domain.user.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suzzingv.suzzingv.rockstar.global.db.DBMarkers.MainEntity;

@MainEntity
@Entity
@Table(name = "user_fcm_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserFcm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String fcmToken;

    @Builder
    private UserFcm(Long userId, String fcmToken) {
        this.userId = userId;
        this.fcmToken = fcmToken;
    }

    public void changeFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
