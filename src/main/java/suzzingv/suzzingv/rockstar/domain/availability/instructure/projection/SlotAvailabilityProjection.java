package suzzingv.suzzingv.rockstar.domain.availability.instructure.projection;

import java.time.Instant;
import java.time.OffsetDateTime;

public interface SlotAvailabilityProjection {
    Instant getSlotStart();
    Instant getSlotEnd();
    Integer getAvailableCount();
}
