package suzzingv.suzzingv.rockstar.domain.schedule.appplication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;
import suzzingv.suzzingv.rockstar.domain.schedule.infrastructure.ScheduleRepository;
import suzzingv.suzzingv.rockstar.domain.schedule.presentation.dto.res.ScheduleResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public Page<ScheduleResponse> getByBand(Long bandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<Schedule> scheduleList = scheduleRepository.findByBandIdOrderByDateDesc(bandId, pageable);

        return scheduleList.map(ScheduleResponse::from);
    }
}
