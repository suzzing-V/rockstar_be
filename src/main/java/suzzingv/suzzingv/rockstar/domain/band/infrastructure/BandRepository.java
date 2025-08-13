package suzzingv.suzzingv.rockstar.domain.band.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.Band;

import java.util.List;
import java.util.Optional;

public interface BandRepository extends JpaRepository<Band, Long> {

    Optional<Band> findByInvitationUrl(String invitationUrl);

    List<Band> findByManagerId(Long userId);
}
