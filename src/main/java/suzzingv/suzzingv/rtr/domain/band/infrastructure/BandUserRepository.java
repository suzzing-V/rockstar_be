package suzzingv.suzzingv.rtr.domain.band.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.Band;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.BandUser;

import java.util.Optional;

@Repository
public interface BandUserRepository extends JpaRepository<BandUser, Long> {
}
