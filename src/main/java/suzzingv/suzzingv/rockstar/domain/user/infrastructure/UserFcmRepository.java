package suzzingv.suzzingv.rockstar.domain.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.UserFcm;
import suzzingv.suzzingv.rockstar.global.db.DBMarkers.MainRepository;

import java.util.Optional;

@MainRepository
public interface UserFcmRepository extends JpaRepository<UserFcm, Long> {
    Optional<UserFcm> findByUserId(Long userId);
}
