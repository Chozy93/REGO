package com.itwillbs.security;

import com.itwillbs.entity.User;
import com.itwillbs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /* =========================
       비밀번호 인코딩
       - 회원가입
       - 비밀번호 변경
    ========================= */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /* =========================
       비밀번호 검증
       - 로그인
       - 탈퇴
       - 민감 정보 수정 전
    ========================= */
    public void verifyPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    /* =========================
       현재 비밀번호 검증 (User 기준)
    ========================= */
    public void verifyPassword(User user, String rawPassword) {
        verifyPassword(rawPassword, user.getPassword());
    }

    /* =========================
       비밀번호 변경
    ========================= */
    public void changePassword(User user, String newRawPassword) {
        String encoded = encodePassword(newRawPassword);
        user.changePassword(encoded);
        userRepository.save(user);
    }
}
