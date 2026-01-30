package com.itwillbs.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.itwillbs.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ChatHandshakeInterceptor implements HandshakeInterceptor {

	  @Override
	    public boolean beforeHandshake(
	            ServerHttpRequest request,
	            ServerHttpResponse response,
	            WebSocketHandler wsHandler,
	            Map<String, Object> attributes
	    ) {

	        if (request instanceof ServletServerHttpRequest servletRequest) {

	            HttpServletRequest httpRequest =
	                    servletRequest.getServletRequest();

	            var session = httpRequest.getSession(false);
	            if (session == null) {
	                return true; // ❗ 연결은 허용 (차단 X)
	            }

	            Object securityContext =
	                    session.getAttribute("SPRING_SECURITY_CONTEXT");

	            if (securityContext instanceof org.springframework.security.core.context.SecurityContext context) {

	                Authentication auth = context.getAuthentication();

	                if (auth != null && auth.isAuthenticated()
	                        && auth.getPrincipal() instanceof CustomUserDetails user) {

	                    attributes.put("userId", user.getUserId());
	                }
	            }
	        }

	        return true;
	    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        // 필요 없음 (빈 구현)
    }
}

