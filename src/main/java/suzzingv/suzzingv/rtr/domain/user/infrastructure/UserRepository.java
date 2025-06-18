package suzzingv.suzzingv.rtr.domain.user.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rtr.domain.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNum(String phoneNum);

    Optional<User> findByNickName(String nickName);
}
