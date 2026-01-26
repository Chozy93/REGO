package com.itwillbs.domain;

import java.time.format.DateTimeFormatter;

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

    private final String messageTypeCode;   // TEXT / SYSTEM / IMAGE
    private final String messageTypeLabel;  // 화면 표시용 (선택)

    private final boolean isRead;

    private final String readAt;     // yyyy-MM-dd HH:mm
    private final String createdAt;  // yyyy-MM-dd HH:mm

    /* =========================
       Entity → VO (출력 전용)
    ========================= */
    public ChatMessageVO(ChatMessage entity) {

        this.messageId = entity.getMessageId();
        this.roomId = entity.getChatRoom().getRoomId();

        this.senderId = entity.getSender() != null
                ? entity.getSender().getUserId()
                : 0L;

        this.content = entity.getContent();

        this.messageTypeCode = entity.getMessageType().name();
        this.messageTypeLabel = entity.getMessageType().getLabel();

        this.isRead = entity.isRead();

        this.readAt = entity.getReadAt() != null
                ? entity.getReadAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "";

        this.createdAt = entity.getCreatedAt() != null
                ? entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "";
    }
}
