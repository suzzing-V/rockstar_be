package suzzingv.suzzingv.bandservice.band.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "band_TB")
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    private Long leaderId;

    private String image;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Band(Long leaderId, String name, String image, String introduction) {
        this.leaderId = leaderId;
        this.name = name;
        this.image = image;
        this.introduction = introduction;
    }
}
