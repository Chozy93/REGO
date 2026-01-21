package com.itwillbs.security;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage;

        if (exception instanceof DisabledException) {
            errorMessage = "탈퇴 처리된 계정입니다. 관리자에게 문의하세요.";
        } else if (exception instanceof LockedException) {
            errorMessage = "정지된 계정입니다. 이용이 제한됩니다.";
        } else {
            errorMessage = "이메일 또는 비밀번호가 맞지 않습니다.";
        }

        // 에러 메시지를 URL 파라미터로 안전하게 인코딩해서 보냄
        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}