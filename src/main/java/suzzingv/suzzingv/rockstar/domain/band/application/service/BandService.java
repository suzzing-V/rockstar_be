package suzzingv.suzzingv.rockstar.domain.band.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.BandUser;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Entry;
import suzzingv.suzzingv.rockstar.domain.band.exception.BandException;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandRepository;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.BandUserRepository;
import suzzingv.suzzingv.rockstar.domain.band.infrastructure.EntryRepository;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.BandManagerRequest;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.BandNameRequest;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.EntryAcceptRequest;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res.*;
import suzzingv.suzzingv.rockstar.domain.band.util.UrlUtil;
import suzzingv.suzzingv.rockstar.domain.news.application.NewsService;
import suzzingv.suzzingv.rockstar.domain.notification.application.NotificationService;
import suzzingv.suzzingv.rockstar.domain.schedule.appplication.ScheduleService;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BandService {

    private final BandRepository bandRepository;
    private final UserRepository userRepository;
    private final EntryRepository entryRepository;
    private final BandUserRepository bandUserRepository;
    private final NewsService newsService;
    private final ScheduleService scheduleService;
    private final NotificationService notificationService;

    public BandIdResponse createBand(Long userId, BandRequest request) {
        findUserById(userId);
        String invitationUrl = UrlUtil.generateInvitationUrl();
        Band band = Band.builder()
            .managerId(userId)
            .name(request.getName())
//            .image(request.getImage())
//            .introduction(request.getImage())
            .invitationUrl(invitationUrl)
            .build();
        bandRepository.save(band);

        return BandIdResponse.from(band.getId());
    }

    public BandNameResponse findBandByInvitationUrl(String url) {
        Band band = findByInvitationUrl(url);
        return BandNameResponse.of(band.getId(), band.getName());
    }

    public BandNameResponse applyForEntry(User user, Long bandId) {
        Band band = findById(bandId);
        findUserById(user.getId());

        Entry entry = Entry.builder()
            .bandId(bandId)
            .userId(user.getId())
            .build();
        entryRepository.save(entry);

        notificationService.createEntryApplyNotification(band, user);
        return BandNameResponse.of(bandId, band.getName());
    }

    public void createBandUser(Long bandId, Long userId) {
        BandUser bandUser = BandUser.builder()
                .bandId(bandId)
                .userId(userId)
                .build();
        bandUserRepository.save(bandUser);
    }

    public List<EntryApplicationResponse> getEntryApplicationList(Long userId, Long bandId) {
        Band band = findById(bandId);
        isManager(userId, band.getManagerId());

        List<Entry> entries = entryRepository.findByBandId(bandId);
        List<EntryApplicationResponse> responses = entries.stream()
                .map(entry -> {
                    User user = findUserById(entry.getUserId());
                    return EntryApplicationResponse.of(userId, user.getNickName());
                })
                .collect(Collectors.toList());
        return responses;
    }

    public EntryAcceptResponse acceptEntry(Long managerId, EntryAcceptRequest request) {
        Band band = findById(request.getBandId());
        User user = findUserById(request.getUserId());

        isManager(managerId, band.getId());
        BandUser bandUser = BandUser.builder()
                .userId(user.getId())
                .bandId(band.getId())
                .build();
        bandUserRepository.save(bandUser);

        entryRepository.deleteByUserIdAndBandId(user.getId(), band.getId());

        notificationService.createEntryAcceptNotification(band, user);

        return EntryAcceptResponse.from(user.getId());
    }

    public void deleteEntry(Long managerId, EntryAcceptRequest request) {
        Long bandId = request.getBandId();
        Long userId = request.getUserId();

        isManager(managerId, bandId);
        entryRepository.deleteByUserIdAndBandId(userId, bandId);
    }

    public void isManager(Long userId, Long managerId) {
        if(!Objects.equals(userId, managerId)) {
            throw new BandException(ErrorCode.MANAGER_REQUIRED);
        }
    }

    public Band findById(Long bandId) {
        return bandRepository.findById(bandId)
            .orElseThrow(() -> new BandException(ErrorCode.BAND_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private Band findByInvitationUrl(String url) {
        return bandRepository.findByInvitationUrl(url)
            .orElseThrow(() -> new BandException(ErrorCode.BAND_NOT_FOUND));
    }

    public void isBandMember(Long bandId, Long userId) {
        bandUserRepository.findByBandIdAndUserId(bandId, userId)
                .orElseThrow(() -> new BandException(ErrorCode.NOT_BAND_MEMBER));
    }

    @Transactional(readOnly = true)
    public Page<BandShortInfoResponse> getListByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<BandUser> byUserId = bandUserRepository.findByUserIdOrderById(userId, pageable);
        Page<BandShortInfoResponse> responses = byUserId.map(bandUser -> {
                    Band band = findById(bandUser.getBandId());
                    boolean isManager = false;
                    if(Objects.equals(band.getManagerId(), userId)) isManager = true;

                    return BandShortInfoResponse.of(bandUser.getBandId(), band.getName(), isManager);
                });

        return responses;
    }

    public BandUrlResponse getInvitationUrl(Long bandId) {
        Band band = findById(bandId);

        return BandUrlResponse.from(band);
    }

    public void deleteEntryByUserId(Long userId) {
        entryRepository.deleteByUserId(userId);
    }

    public void delegateManagerOfUserId(Long userId) {
        bandRepository.findByManagerId(userId).forEach(band -> {
            Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "userId"));
            Optional<BandUser> firstUser = bandUserRepository
                    .findByBandId(band.getId(), pageable)
                    .stream()
                    .findFirst();

            firstUser.ifPresent(bandUser -> band.changeManagerId(bandUser.getUserId()));
        });
    }

    public BandIdResponse updateBandName(BandNameRequest request) {
        Band band = findById(request.getBandId());
        band.changeName(request.getName());

        return BandIdResponse.from(band.getId());
    }

    public BandResponse getBandInfo(Long bandId) {
        Band band = findById(bandId);

        return BandResponse.from(band);
    }

    public void withdrawBand(Long userId, Long bandId) {
        Band band = findById(bandId);
        canWithdraw(userId, band.getManagerId());

        bandUserRepository.deleteByBandIdAndUserId(bandId, userId);
    }

    private static void canWithdraw(Long userId, Long managerId) {
        if(managerId.equals(userId)) {
            throw new BandException(ErrorCode.MANAGER_CANT_WITHDRAW);
        }
    }

    public void deleteBand(Long bandId) {
        bandRepository.deleteById(bandId);
        bandUserRepository.deleteByBandId(bandId);
        entryRepository.deleteByBandId(bandId);
        newsService.deleteByBandId(bandId);
        scheduleService.deleteByBandId(bandId);
    }

    public BandIdResponse updateBandManager(BandManagerRequest request) {
        Band band = findById(request.getBandId());
        isBandMember(request.getBandId(), request.getNewManagerId());

        band.changeManagerId(request.getNewManagerId());
        return BandIdResponse.from(request.getBandId());
    }
}
