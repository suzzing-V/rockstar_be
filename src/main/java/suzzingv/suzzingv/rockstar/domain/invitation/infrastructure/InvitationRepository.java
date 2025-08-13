package suzzingv.suzzingv.rockstar.domain.invitation.infrastructure;

import ch.qos.logback.core.testUtil.MockInitialContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.invitation.domain.entity.Invitation;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findByBandId(Long bandId);

    Optional<Invitation> findByBandIdAndUserId(Long bandId, Long userId);

    void deleteByUserIdAndBandId(Long userId, Long bandId);

    void deleteByUserId(Long userId);

    void deleteByBandId(Long bandId);

    List<Invitation> findByUserId(Long userId);
}
