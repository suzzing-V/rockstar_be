package suzzingv.suzzingv.rtr.domain.band.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.BandUser;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.Entry;
import suzzingv.suzzingv.rtr.domain.band.exception.BandException;
import suzzingv.suzzingv.rtr.domain.band.infrastructure.BandRepository;
import suzzingv.suzzingv.rtr.domain.band.infrastructure.BandUserRepository;
import suzzingv.suzzingv.rtr.domain.band.infrastructure.EntryRepository;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandNameResponse;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandResponse;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.EntryApplicationResponse;
import suzzingv.suzzingv.rtr.domain.band.util.UrlUtil;
import suzzingv.suzzingv.rtr.domain.user.domain.entity.User;
import suzzingv.suzzingv.rtr.domain.user.exception.UserException;
import suzzingv.suzzingv.rtr.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rtr.global.response.properties.ErrorCode;

import java.util.ArrayList;
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
            .image(request.getImage())
            .introduction(request.getImage())
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
                    return EntryApplicationResponse.of(user.getNickName());
                })
                .collect(Collectors.toList());
        return responses;
    }

    private void isManager(Long userId, Long managerId) {
        if(!Objects.equals(userId, managerId)) {
            throw new BandException(ErrorCode.MANAGER_REQUIRED);
        }
    }

    private Band findById(Long bandId) {
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
}
