package com.itwillbs.security.util;

import com.itwillbs.domain.user.UserVO;
import com.itwillbs.entity.User;
import com.itwillbs.security.CustomUserDetails;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
        // 유틸 클래스 생성 방지
    }
    
    
    public static boolean isLogined() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (!authentication.isAuthenticated()) {
            return false;
        }

        // AnonymousUser 방지
        return !(authentication instanceof AnonymousAuthenticationToken);
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
    현재 로그인 User (Entity) 반환
    - Service 계층 전용
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
    현재 로그인 UserVO 반환
    - View / Controller 전용
 ========================= */
 public static UserVO getCurrentUserVO() {

     User user = getCurrentUser();

     if (user == null) {
         return null;
     }

     return new UserVO(user); // ✅ 변환 책임 여기서 종료
 }
    /* =========================
       현재 로그인 UserId 반환
    ========================= */
    public static Long getCurrentUserId() {

        User user = getCurrentUser();
        return (user != null) ? user.getUserId() : null;
    }
}
