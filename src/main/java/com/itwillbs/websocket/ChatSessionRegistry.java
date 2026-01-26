package com.itwillbs.websocket;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ChatSessionRegistry {

    // roomId -> 접속 중인 userId set
    private final Map<Long, Set<Long>> activeUsersByRoom = new ConcurrentHashMap<>();
    
    
 // roomId -> 채팅방 화면을 실제로 보고 있는 유저
    private final Map<Long, Set<Long>> viewingUsersByRoom = new ConcurrentHashMap<>();
    
    
    
    public void markViewing(Long roomId, Long userId) {
        viewingUsersByRoom
            .computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
            .add(userId);
    }

    public void unmarkViewing(Long roomId, Long userId) {
        Set<Long> users = viewingUsersByRoom.get(roomId);
        if (users != null) {
            users.remove(userId);
        }
    }

    public boolean isViewingRoom(Long roomId, Long userId) {
        return viewingUsersByRoom
                .getOrDefault(roomId, Set.of())
                .contains(userId);
    }

    
    
    
    public void join(Long roomId, Long userId) {
        activeUsersByRoom
            .computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
            .add(userId);
    }

    public void leave(Long roomId, Long userId) {
        Set<Long> users = activeUsersByRoom.get(roomId);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) {
                activeUsersByRoom.remove(roomId);
            }
        }
    }

    public boolean isUserActiveInRoom(Long roomId, Long userId) {
        return activeUsersByRoom
                .getOrDefault(roomId, Set.of())
                .contains(userId);
    }
}
