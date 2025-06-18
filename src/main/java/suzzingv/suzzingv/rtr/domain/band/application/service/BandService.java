package suzzingv.suzzingv.rtr.domain.band.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandResponse;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rtr.domain.band.exception.BandException;
import suzzingv.suzzingv.rtr.domain.band.infrastructure.BandRepository;

import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandIdResponse;
import suzzingv.suzzingv.rtr.domain.band.util.BandShareLinkUtil;
import suzzingv.suzzingv.rtr.domain.user.domain.entity.User;
import suzzingv.suzzingv.rtr.domain.user.exception.UserException;
import suzzingv.suzzingv.rtr.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rtr.global.response.properties.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BandService {

    private final BandRepository bandRepository;
    private final UserRepository userRepository;

    public BandResponse createBand(Long userId, BandRequest request) {
        findUserById(userId);
        String url = BandShareLinkUtil.generateShareLink();
        Band band = Band.builder()
                .leaderId(userId)
                .name(request.getName())
                .image(request.getImage())
                .introduction(request.getImage())
                .url(url)
                .build();
        bandRepository.save(band);

        return BandResponse.from(band.getId());
    }

    public BandIdResponse getBandUrl(String url) {
        Band band = findByUrl(url);
        return BandIdResponse.from(band.getId());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private Band findByUrl(String url) {
        return bandRepository.findByUrl(url)
            .orElseThrow(() -> new BandException(ErrorCode.BAND_NOT_FOUND));
    }
}
