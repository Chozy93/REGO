package com.itwillbs.websocket;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatSessionEventListener {

    private final ChatSessionRegistry chatSessionRegistry;

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {

        SimpMessageHeaderAccessor accessor =
                SimpMessageHeaderAccessor.wrap(event.getMessage());

        String destination = accessor.getDestination();

        if (destination == null) return;

        Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
        if (sessionAttrs == null) return;

        Long userId = (Long) sessionAttrs.get("userId"); // ✅ 여기
        if (userId == null) {
            throw new IllegalStateException("WebSocket userId not found in session");
        }

        Long roomId = extractRoomId(destination);

        chatSessionRegistry.join(roomId, userId);
    }


    @EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {

        SimpMessageHeaderAccessor accessor =
                SimpMessageHeaderAccessor.wrap(event.getMessage());

        String destination = accessor.getDestination();
        if (destination == null) return;

        Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
        if (sessionAttrs == null) return;

        Long userId = (Long) sessionAttrs.get("userId");
        if (userId == null) {
            throw new IllegalStateException("WebSocket userId not found in session");
        }

        Long roomId = extractRoomId(destination);

        chatSessionRegistry.leave(roomId, userId);
    }

 
    /**
     * /topic/chat.123 -> 123
     */
    private Long extractRoomId(String destination) {
        return Long.valueOf(
                destination.substring(destination.lastIndexOf('.') + 1)
        );
    }
}
