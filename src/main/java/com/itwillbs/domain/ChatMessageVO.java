package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.ChatMessage;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageVO {

    private final Long messageId;
    private final Long roomId;
    private final Long senderId;
    private final String content;
    private final boolean isRead;
    private final LocalDateTime readAt;
    private final LocalDateTime createdAt;

    /* =========================
       Entity → VO (조회)
    ========================= */
    public ChatMessageVO(ChatMessage entity) {
        this.messageId = entity.getMessageId();
        this.roomId = entity.getChatRoom().getRoomId();
        this.senderId = entity.getSender().getUserId();
        this.content = entity.getContent();
        this.isRead = entity.isRead();
        this.readAt = entity.getReadAt();
        this.createdAt = entity.getCreatedAt();
    }

    /* =========================
       전송용
    ========================= */
    public ChatMessageVO(String content) {
        this.messageId = null;
        this.roomId = null;
        this.senderId = null;
        this.content = content;
        this.isRead = false;
        this.readAt = null;
        this.createdAt = null;
    }
}
