package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.ChatRoom;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomVO {

    private final Long roomId;
    private final Long productId;
    private final Long buyerId;
    private final Long sellerId;
    private final String roomStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO (조회)
    ========================= */
    public ChatRoomVO(ChatRoom entity) {
        this.roomId = entity.getRoomId();
        this.productId = entity.getProduct().getProductId();
        this.buyerId = entity.getBuyer().getUserId();
        this.sellerId = entity.getSeller().getUserId();
        this.roomStatus = entity.getRoomStatus().name();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
       생성 요청용 (room_id는 없음)
    ========================= */
    public ChatRoomVO() {
        this.roomId = null;
        this.productId = null;
        this.buyerId = null;
        this.sellerId = null;
        this.roomStatus = null;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
