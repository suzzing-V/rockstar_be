package suzzingv.suzzingv.rockstar.domain.invitation.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.invitation.domain.entity.Invitation;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findByBandId(Long bandId);

    void deleteByUserIdAndBandId(Long userId, Long bandId);

    void deleteByUserId(Long userId);

    void deleteByBandId(Long bandId);
}
