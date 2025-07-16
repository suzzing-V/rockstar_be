package suzzingv.suzzingv.rockstar.domain.band.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.band.domain.entity.BandUser;
import suzzingv.suzzingv.rockstar.domain.schedule.domain.Schedule;

import java.util.List;

@Repository
public interface BandUserRepository extends JpaRepository<BandUser, Long> {
    Page<BandUser> findByUserIdOrderById(Long userId, Pageable pageable);
}
