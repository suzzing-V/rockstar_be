package suzzingv.suzzingv.rockstar.domain.band.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.BandUser;

import java.util.List;

@Repository
public interface BandUserRepository extends JpaRepository<BandUser, Long> {
    List<Long> findByUserId(Long userId);
}
