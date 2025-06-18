package suzzingv.suzzingv.rtr.domain.band.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(nullable = false)
    private String url;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Band(Long leaderId, String name, String image, String introduction, String url) {
        this.leaderId = leaderId;
        this.name = name;
        this.image = image;
        this.introduction = introduction;
        this.url = url;
    }
}
