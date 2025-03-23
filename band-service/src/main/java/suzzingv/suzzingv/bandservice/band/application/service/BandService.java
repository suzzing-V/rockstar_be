package suzzingv.suzzingv.bandservice.band.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.bandservice.band.domain.entity.Band;
import suzzingv.suzzingv.bandservice.band.infrastructure.BandRepository;
import suzzingv.suzzingv.bandservice.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.bandservice.band.presentation.dto.res.BandResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class BandService {

    private final BandRepository bandRepository;

    public BandResponse createBand(Long userId, BandRequest request) {

        Band band = Band.builder()
                .leaderId(userId)
                .name(request.getName())
                .image(request.getImage())
                .introduction(request.getImage())
                .build();
        bandRepository.save(band);

        return BandResponse.builder()
                .bandId(band.getId())
                .build();
    }
}
