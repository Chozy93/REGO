package com.itwillbs.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAjaxLoginSuccessHandler
        implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");

        response.getWriter().write("""
        {
          "success": true
        }
        """);
        response.getWriter().flush();
    }
}
