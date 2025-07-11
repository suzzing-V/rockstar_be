package suzzingv.suzzingv.rockstar.domain.band.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.BandUser;

@Repository
public interface BandUserRepository extends JpaRepository<BandUser, Long> {
}
