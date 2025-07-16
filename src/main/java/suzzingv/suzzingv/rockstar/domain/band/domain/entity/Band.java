package suzzingv.suzzingv.rockstar.domain.band.domain.entity;

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
    private Long managerId;

//    private String image;

    @Column(nullable = false)
    private String invitationUrl;

//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String introduction;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Band(Long managerId, String name, String invitationUrl) {
        this.managerId = managerId;
        this.name = name;
//        this.image = image;
//        this.introduction = introduction;
        this.invitationUrl = invitationUrl;
    }
}
