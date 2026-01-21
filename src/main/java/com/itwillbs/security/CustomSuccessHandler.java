package com.itwillbs.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.itwillbs.entity.User;
import com.itwillbs.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(); // google, naver 등
        Map<String, Object> attributes = oAuth2User.getAttributes();

  
        String providerId = "";
        if (registrationId.equals("kakao")) {
            providerId = attributes.get("id").toString();
        } else if (registrationId.equals("naver")) {
            providerId = ((Map<String, Object>) attributes.get("response")).get("id").toString();
        } else {
            providerId = attributes.get("sub").toString();
        }
        

        String finalProviderId = providerId.length() > 20 ? providerId.substring(0, 20) : providerId;
        String dbUsername = registrationId + "_" + finalProviderId;

        System.out.println("🚩 핸들러에서 재구성한 DB 아이디: " + dbUsername);

        User user = userService.findByUsername(dbUsername); 

        if (user == null) {

            System.out.println("🚩 유저를 못 찾았어요! 안전하게 추가 정보 페이지로 보냅니다.");
            response.sendRedirect("/complete-info");
            return;
        }

        String phone = user.getPhoneNumber();
        if (phone == null || phone.isEmpty() || phone.equals("PENDING") || phone.startsWith("TMP_")) {
            System.out.println("🚩 정보 미기입 유저 확정: " + phone);
            response.sendRedirect("/complete-info");
            return;
        }
        
        response.sendRedirect("/");    
    }
}