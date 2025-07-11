package suzzingv.suzzingv.rockstar.domain.band.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "band_user_TB")
@Getter
public class BandUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long bandId;

    @Builder
    private BandUser(Long userId, Long bandId) {
        this.userId = userId;
        this.bandId = bandId;
    }
}
