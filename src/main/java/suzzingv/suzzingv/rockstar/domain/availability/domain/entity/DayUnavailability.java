package suzzingv.suzzingv.rockstar.domain.availability.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import suzzingv.suzzingv.rockstar.domain.availability.domain.vo.TimeRange;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "day_unavailability_tb")
@Getter
public class DayUnavailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "tz", nullable = false)
    private String tz = "Asia/Seoul";

    @Column(nullable = false)
    private boolean isAllDay;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private List<TimeRange> range;

    @Builder
    private DayUnavailability(LocalDate date, Long userId, boolean isAllDay, List<TimeRange> range) {
        this.date = date;
        this.userId = userId;
        this.isAllDay = isAllDay;
        this.range = range;
    }
}
