package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.ChatMessageVO;

@Entity
@Table(name = "chat_message")
@Getter
public class ChatMessage {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    /* =========================
       채팅방
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "room_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_chat_message_room")
    )
    private ChatRoom chatRoom;

    /* =========================
       발신자
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sender_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_chat_message_sender")
    )
    private User sender;

    /* =========================
       메시지 내용
    ========================= */
    @Column(name = "content", nullable = false)
    private String content;

    /* =========================
       읽음 여부 (상대방 기준)
    ========================= */
    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    /* =========================
       전송 일시
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected ChatMessage() {}

    /* =========================
       생성자 (메시지 전송)
    ========================= */
    public ChatMessage(ChatRoom chatRoom, User sender, ChatMessageVO vo) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = vo.getContent();
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public ChatMessageVO toVO() {
        return new ChatMessageVO(this);
    }

    /* =========================
       읽음 처리
    ========================= */
    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = LocalDateTime.now();
        }
    }
}
