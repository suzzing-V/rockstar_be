package suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule_request_tb")
public class ScheduleRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bandId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    private ScheduleRequest(Long bandId, LocalDate startDate, LocalDate endDate) {
        this.bandId = bandId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
