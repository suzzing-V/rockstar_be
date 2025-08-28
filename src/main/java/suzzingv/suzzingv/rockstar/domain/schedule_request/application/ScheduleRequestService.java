package suzzingv.suzzingv.rockstar.domain.schedule_request.application;

import com.google.firebase.internal.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandRepository;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandUserRepository;
import suzzingv.suzzingv.rockstar.domain.notification.application.NotificationService;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequest;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity.ScheduleRequestAssignees;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.enums.RequestStatus;
import suzzingv.suzzingv.rockstar.domain.schedule_request.exception.ScheduleRequestException;
import suzzingv.suzzingv.rockstar.domain.schedule_request.infrastructure.ScheduleRequestAssigneesRepository;
import suzzingv.suzzingv.rockstar.domain.schedule_request.infrastructure.ScheduleRequestRepository;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.req.ScheduleRequestRequest;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ScheduleRequestIdResponse;
import suzzingv.suzzingv.rockstar.global.firebase.FcmService;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleRequestService {

    private final ScheduleRequestRepository scheduleRequestRepository;
    private final ScheduleRequestAssigneesRepository requestAssigneesRepository;
    private final BandUserRepository bandUserRepository;
    private final BandService bandService;
    private final FcmService fcmService;
    private final NotificationService notificationService;

    public ScheduleRequestIdResponse createScheduleRequest(ScheduleRequestRequest request) {
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .bandId(request.getBandId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        scheduleRequestRepository.save(scheduleRequest);

        Band band = bandService.findById(request.getBandId());
        createMemberStatus(band, scheduleRequest);
        notificationService.createScheduleRequestNotification(band, scheduleRequest);

        return ScheduleRequestIdResponse.from(scheduleRequest.getId());
    }

    private void createMemberStatus(Band band, ScheduleRequest scheduleRequest) {
        bandUserRepository.findByBandId(band.getId())
                .forEach(bandUser -> {
                    Long userId = bandUser.getUserId();
                    ScheduleRequestAssignees scheduleRequestAssignees = ScheduleRequestAssignees.builder()
                            .requestId(scheduleRequest.getId())
                            .userId(userId)
                            .status(RequestStatus.PENDING)
                            .build();
                    requestAssigneesRepository.save(scheduleRequestAssignees);

                    fcmService.sendScheduleRequestPush(userId, band, scheduleRequest);
                });
    }

    public ScheduleRequestIdResponse completeRequest(Long userId, Long requestId) {
        ScheduleRequestAssignees scheduleRequestAssignees = findScheduleReqeustAssigneesByUserIdAndReqeustId(userId, requestId);
        scheduleRequestAssignees.completeRequest();
        return null;
    }

    private ScheduleRequestAssignees findScheduleReqeustAssigneesByUserIdAndReqeustId(Long userId, Long requestId) {
        ScheduleRequestAssignees scheduleRequestAssignees = requestAssigneesRepository.findByRequestIdAndUserId(requestId, userId)
                .orElseThrow(() -> new ScheduleRequestException(ErrorCode.SCHEDULE_REQUEST_ASSIGNEES_NOT_FOUND));
        return scheduleRequestAssignees;
    }
}
