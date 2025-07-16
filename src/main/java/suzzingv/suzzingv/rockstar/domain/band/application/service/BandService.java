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
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.req.EntryAcceptRequest;
import suzzingv.suzzingv.rockstar.domain.band.presentation.dto.res.*;
import suzzingv.suzzingv.rockstar.domain.band.util.UrlUtil;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;

import java.util.List;
import java.util.Objects;
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

    public BandResponse createBand(Long userId, BandRequest request) {
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

        return BandResponse.from(band.getId());
    }

    public BandNameResponse getInvitationUrl(String url) {
        Band band = findByInvitationUrl(url);
        return BandNameResponse.of(band.getId(), band.getName());
    }

    public BandNameResponse applyForEntry(Long userId, Long bandId) {
        Band band = findById(bandId);
        findUserById(userId);

        Entry entry = Entry.builder()
            .bandId(bandId)
            .userId(userId)
            .build();
        entryRepository.save(entry);
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
        Long bandId = request.getBandId();
        Long userId = request.getUserId();

        isManager(managerId, bandId);
        BandUser bandUser = BandUser.builder()
                .userId(userId)
                .bandId(bandId)
                .build();
        bandUserRepository.save(bandUser);

        entryRepository.deleteByUserIdAndBandId(userId, bandId);

        // 푸시 큐 구독

        return EntryAcceptResponse.from(userId);
    }

    public void deleteEntry(Long managerId, EntryAcceptRequest request) {
        Long bandId = request.getBandId();
        Long userId = request.getUserId();

        isManager(managerId, bandId);
        entryRepository.deleteByUserIdAndBandId(userId, bandId);
    }

    private void isManager(Long userId, Long managerId) {
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
}
