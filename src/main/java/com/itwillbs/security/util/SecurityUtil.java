package com.itwillbs.security.util;

import com.itwillbs.entity.User;
import com.itwillbs.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
        // 유틸 클래스 생성 방지
    }

    /* =========================
       인증 여부 확인
    ========================= */
    public static boolean isAuthenticated() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /* =========================
       현재 로그인 User 반환
    ========================= */
    public static User getCurrentUser() {

        if (!isAuthenticated()) {
            return null;
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }

        return null;
    }

    /* =========================
       현재 로그인 UserId 반환
    ========================= */
    public static Long getCurrentUserId() {

        User user = getCurrentUser();
        return (user != null) ? user.getUserId() : null;
    }
}
