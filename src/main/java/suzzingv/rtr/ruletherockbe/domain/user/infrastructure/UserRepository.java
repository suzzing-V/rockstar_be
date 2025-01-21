package suzzingv.rtr.ruletherockbe.domain.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.rtr.ruletherockbe.domain.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
