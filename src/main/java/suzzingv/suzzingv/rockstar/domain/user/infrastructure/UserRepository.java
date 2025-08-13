package suzzingv.suzzingv.rockstar.domain.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import suzzingv.suzzingv.rockstar.domain.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    Optional<User> findById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.phoneNum = :phoneNum AND u.deletedAt IS NULL")
    Optional<User> findByPhoneNum(@Param("phoneNum") String phoneNum);

    @Query("SELECT u FROM User u WHERE u.nickName = :nickName AND u.deletedAt IS NULL")
    Optional<User> findByNickName(@Param("nickName") String nickName);
}
