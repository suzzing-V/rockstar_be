package suzzingv.suzzingv.rtr.domain.band.application.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.req.BandRequest;
import suzzingv.suzzingv.rtr.domain.band.presentation.dto.res.BandResponse;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rtr.domain.band.exception.BandException;
import suzzingv.suzzingv.rtr.domain.band.infrastructure.BandRepository;

import java.util.Objects;
import suzzingv.suzzingv.rtr.global.response.properties.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BandService {

    private final BandRepository bandRepository;

    public BandResponse createBand(Long userId, BandRequest request) {
        isUserExist(userId);
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

    private void isUserExist(Long userId) {
//        try {
//            userGrpcClinent.isUserExist(userId);
//        } catch (StatusRuntimeException e) {
//            Status.Code code = e.getStatus().getCode();
//            if (Objects.requireNonNull(code) == Status.Code.NOT_FOUND) {
//                throw new BandException(ErrorCode.USER_NOT_FOUND);
//            }
//        }
    }
}
