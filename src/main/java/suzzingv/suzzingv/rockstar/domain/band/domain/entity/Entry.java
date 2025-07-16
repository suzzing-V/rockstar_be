package suzzingv.suzzingv.rockstar.domain.band.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "entry_TB")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bandId;

    @Column(nullable = false)
    private Long userId;

    @Builder
    private Entry(Long bandId, long userId) {
        this.bandId = bandId;
        this.userId = userId;
    }
}
