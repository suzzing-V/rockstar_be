package suzzingv.suzzingv.rockstar.domain.schedule.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suzzingv.suzzingv.rockstar.global.db.DBMarkers.MainEntity;

import java.time.LocalDateTime;

@MainEntity
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule_TB")
@Getter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bandId;

    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private Long dayDiff;

    @Builder
    public Schedule(Long bandId, String description, LocalDateTime startDate, LocalDateTime endDate, String dayOfWeek, Long dayDiff) {
        this.bandId = bandId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayOfWeek = dayOfWeek;
        this.dayDiff = dayDiff;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void changeEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void changeDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void changeDayDiff(Long dayDiff) {
        this.dayDiff = dayDiff;
    }
}
