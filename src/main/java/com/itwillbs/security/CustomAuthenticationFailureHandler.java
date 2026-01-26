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
public class CustomAuthenticationFailureHandler
        implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        String errorMessage;

        if (exception instanceof DisabledException) {
            errorMessage = "탈퇴 처리된 계정입니다. 관리자에게 문의하세요.";
        } else if (exception instanceof LockedException) {
            errorMessage = "정지된 계정입니다. 이용이 제한됩니다.";
        } else {
            errorMessage = "이메일 또는 비밀번호가 맞지 않습니다.";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().write("""
        {
          "success": false,
          "message": "%s"
        }
        """.formatted(errorMessage));
    }
}
