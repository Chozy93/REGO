package com.itwillbs.security;

import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /* =========================
       권한
    ========================= */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // hasRole("USER") → 내부적으로 ROLE_USER 기대
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    /* =========================
       인증 정보
    ========================= */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 로그인 ID는 email 기준
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /* =========================
       계정 상태
    ========================= */
    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 정책 없음
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 정책 없음
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 정지(BANNED)면 잠금
        return user.getUserStatus() != UserStatus.BANNED;
    }

    @Override
    public boolean isEnabled() {
        // ACTIVE만 로그인 허용
        return user.getUserStatus() == UserStatus.ACTIVE;
    }

    /* =========================
       내부 User 접근용
    ========================= */
    public User getUser() {
        return user;
    }
}
