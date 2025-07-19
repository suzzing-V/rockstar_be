package suzzingv.suzzingv.rockstar.domain.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import suzzingv.suzzingv.rockstar.domain.user.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_TB")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    @Pattern(regexp = "^[0-9]*$")
    private String phoneNum;

    @Column(length = 10)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @Builder
    private User(Role role,
        String phoneNum) {
        this.role = role;
        this.phoneNum = phoneNum;
    }

    public void changeNickname(String nickname) {
        this.nickName = nickname;
    }

    public void changeDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getText()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return phoneNum;
    }
}
