package suzzingv.suzzingv.rockstar.domain.band.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.BandUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface BandUserRepository extends JpaRepository<BandUser, Long> {
    Page<BandUser> findByUserIdOrderById(Long userId, Pageable pageable);

    Optional<BandUser> findByBandIdAndUserId(Long bandId, Long userId);

    Page<BandUser> findByBandId(Long bandId, Pageable pageable);

    void deleteByUserId(Long userId);
}
