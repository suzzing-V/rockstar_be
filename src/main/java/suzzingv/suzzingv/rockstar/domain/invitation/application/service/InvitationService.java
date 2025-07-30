package suzzingv.suzzingv.rockstar.domain.invitation.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.application.service.BandService;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.invitation.domain.entity.Invitation;
import suzzingv.suzzingv.rockstar.domain.invitation.infrastructure.InvitationRepository;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.InvitationRequest;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationInfoResponse;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationResponse;
import suzzingv.suzzingv.rockstar.domain.invitation.presentation.dto.res.InvitationUserInfoResponse;
import suzzingv.suzzingv.rockstar.domain.notification.application.NotificationService;
import suzzingv.suzzingv.rockstar.domain.user.application.service.UserService;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.global.firebase.FcmService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final NotificationService notificationService;
    private final BandService bandService;
    private final UserService userService;
    private final FcmService fcmService;

    public InvitationResponse invite(InvitationRequest request) {
        log.info("시작");
        Band band = bandService.findById(request.getBandId());
        User user = userService.findUserById(request.getUserId());

        Invitation invitation = Invitation.builder()
                .bandId(request.getBandId())
                .userId(request.getUserId())
                .build();
        invitationRepository.save(invitation);

        notificationService.createInvitationNotification(band, user);

        fcmService.sendInvitationPush(band.getName(), band, user.getId());
        log.info("fcm 후");
        return InvitationResponse.builder()
                .bandId(band.getId())
                .userId(user.getId())
                .build();
    }

    public InvitationUserInfoResponse searchUser(Long bandId, String nickname) {
        User user = userService.findByNickname(nickname);
        boolean isInvited = isInvited(bandId, user);
        boolean isBandMember = bandService.isBandMember(bandId, user.getId());

        return InvitationUserInfoResponse.of(user, !isInvited && !isBandMember);
    }

    private boolean isInvited(Long bandId, User user) {
        return invitationRepository.findByBandIdAndUserId(bandId, user.getId())
                .isPresent();
    }

    public List<InvitationInfoResponse> getInvitationList(Long userId) {
        List<InvitationInfoResponse> responses = invitationRepository.findByUserId(userId)
                .stream().map(invitation -> {
                    Band band = bandService.findById(invitation.getBandId());
                    return InvitationInfoResponse.from(band);
                }).toList();

        return responses;
    }
}
