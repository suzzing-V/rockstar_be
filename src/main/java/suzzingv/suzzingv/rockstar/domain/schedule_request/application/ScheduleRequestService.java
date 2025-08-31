package suzzingv.suzzingv.rockstar.domain.schedule_request.application;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
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
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ScheduleRequestResponse;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.ShortScheduleRequestResponse;
import suzzingv.suzzingv.rockstar.domain.schedule_request.presentation.dto.res.UserSummaryDto;
import suzzingv.suzzingv.rockstar.domain.user.application.service.UserService;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.global.firebase.FcmService;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

import java.util.List;

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
    private final UserService userService;

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

    public ScheduleRequestResponse getScheduleRequestInfo(Long requestId) {
        ScheduleRequest scheduleRequest = getScheduleRequestByRequestId(requestId);
        List<ScheduleRequestAssignees> scheduleRequestAssignees = requestAssigneesRepository.findByRequestId(requestId);
        int totalCount = scheduleRequestAssignees.size();
        List<UserSummaryDto> pendingMembers = getPendingMembers(scheduleRequestAssignees);
        return ScheduleRequestResponse.of(scheduleRequest, totalCount, pendingMembers);
    }

    @NotNull
    private List<UserSummaryDto> getPendingMembers(List<ScheduleRequestAssignees> scheduleRequestAssignees) {
        List<UserSummaryDto> pendingMembers = scheduleRequestAssignees
                .stream()
                .filter(scheduleRequestAssignee -> scheduleRequestAssignee.getStatus() == RequestStatus.PENDING)
                .map(scheduleRequestAssignee -> {
                    User user = userService.findUserById(scheduleRequestAssignee.getUserId());
                    return UserSummaryDto.from(user);
                })
                .toList();
        return pendingMembers;
    }

    private ScheduleRequest getScheduleRequestByRequestId(Long requestId) {
        ScheduleRequest scheduleRequest = scheduleRequestRepository.findById(requestId)
                .orElseThrow(() -> new ScheduleRequestException(ErrorCode.SCHEDULE_REQUEST_NOT_FOUND));
        return scheduleRequest;
    }

    public Page<ShortScheduleRequestResponse> getScheduleRequestsOfBand(Long userId, Long bandId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ShortScheduleRequestResponse> responses = scheduleRequestRepository.findByBandId(bandId, pageable)
                .map(scheduleRequest -> {
                    ScheduleRequestAssignees assignees = findScheduleReqeustAssigneesByUserIdAndReqeustId(userId, scheduleRequest.getId());
                    boolean isCompleted = RequestStatus.isCompleted(assignees.getStatus());
                    return ShortScheduleRequestResponse.of(scheduleRequest, isCompleted);
                });
        return responses;
    }
}
