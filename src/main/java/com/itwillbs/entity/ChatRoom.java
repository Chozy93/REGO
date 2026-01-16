package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.ChatRoomVO;
import com.itwillbs.entity.enumtype.ChatRoomStatus;

@Entity
@Table(
    name = "chat_room",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_chat_room_unique",
            columnNames = {"product_id", "buyer_id", "seller_id"}
        )
    }
)
@Getter
public class ChatRoom {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    /* =========================
       상품
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_chat_room_product")
    )
    private Product product;

    /* =========================
       구매자
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "buyer_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_chat_room_buyer")
    )
    private User buyer;

    /* =========================
       판매자
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "seller_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_chat_room_seller")
    )
    private User seller;

    /* =========================
       상태
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "room_status", nullable = false)
    private ChatRoomStatus roomStatus;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected ChatRoom() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public ChatRoom(Product product, User buyer, User seller, ChatRoomVO vo) {
        this.product = product;
        this.buyer = buyer;
        this.seller = seller;
        this.roomStatus = ChatRoomStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public ChatRoomVO toVO() {
        return new ChatRoomVO(this);
    }

    /* =========================
       상태 변경
    ========================= */
    public void close() {
        this.roomStatus = ChatRoomStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
    }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
