package suzzingv.suzzingv.rockstar.domain.availability.domain.entity;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "unavailable_block_tb",
        indexes = {
                @Index(name = "unavailable_block_user", columnList = "user_id")
        }
)
@Getter
public class UnavailableBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Type(PostgreSQLRangeType.class)
    @Column(name = "period", nullable = false, columnDefinition = "tstzrange")
    private Range<ZonedDateTime> period;

    @Builder
    private UnavailableBlock(Long userId, Range<ZonedDateTime> period) {
        this.userId = userId;
        this.period = period;
    }
}
