package suzzingv.suzzingv.rockstar.domain.user.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNum(String phoneNum);

    Optional<User> findByNickName(String nickName);
}
