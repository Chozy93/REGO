package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.ChatMessage;
import com.itwillbs.entity.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /* =========================
       채팅방 기준 메시지 조회 (시간순)
    ========================= */
    List<ChatMessage> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);
    List<ChatMessage> findByChatRoom_RoomIdOrderByCreatedAtAsc(Long roomId);

}
