package suzzingv.suzzingv.rockstar.domain.availability.presentation.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class TimeSlotWithHeadcountResponse {

    private String startTime;

    private String endTime;

    private Integer availableCount;

    private Integer totalCount;

    public static TimeSlotWithHeadcountResponse of(String startTime, String endTime, int availableCount, int totalCount) {
        return TimeSlotWithHeadcountResponse.builder()
                .startTime(startTime)
                .endTime(endTime)
                .availableCount(availableCount)
                .totalCount(totalCount)
                .build();
    }
}
