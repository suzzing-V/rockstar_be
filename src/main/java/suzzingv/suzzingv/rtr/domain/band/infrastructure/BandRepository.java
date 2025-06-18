package suzzingv.suzzingv.rtr.domain.band.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.Band;

@Repository
public interface BandRepository extends JpaRepository<Band, Long> {

    Optional<Band> findByInvitationUrl(String invitationUrl);
}
