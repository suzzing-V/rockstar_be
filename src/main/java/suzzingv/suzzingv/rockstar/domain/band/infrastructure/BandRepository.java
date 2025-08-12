package suzzingv.suzzingv.rockstar.domain.band.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rockstar.global.db.DBMarkers;

import java.util.List;
import java.util.Optional;

import static suzzingv.suzzingv.rockstar.global.db.DBMarkers.*;

@MainRepository
public interface BandRepository extends JpaRepository<Band, Long> {

    Optional<Band> findByInvitationUrl(String invitationUrl);

    List<Band> findByManagerId(Long userId);
}
