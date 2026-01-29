package com.itwillbs.view.chat;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomHeaderViewVO {

    /* =========================
       채팅방
    ========================= */
    private final Long roomId;
    private final Long productId;
    private String roomType;   // ← 추가 (PRODUCT / ADMIN_DM)
    /* =========================
       채팅 상대
    ========================= */
    private final Long opponentUserId;
    private final String opponentUserName;
    private final String opponentProfileImageUrl;

    /* =========================
       상대방 신뢰도
    ========================= */
    private final double opponentRatingAvg;     // 항상 값 있음 (0.0 포함)
    private final int opponentTotalReviews;      // 항상 값 있음 (0 포함)

    /* =========================
       상품 컨텍스트
    ========================= */
    private final String productName;
    private final int productPrice;

    /* =========================
       채팅방 상태
    ========================= */
    private final String roomStatusCode;
    private final String roomStatusLabel;

    public ChatRoomHeaderViewVO(
            Long roomId,
            Long productId,
            String roomType,
            Long opponentUserId,
            String opponentUserName,
            String opponentProfileImageUrl,

            Double opponentRatingAvg,
            Integer opponentTotalReviews,

            String productName,
            Integer productPrice,

            String roomStatusCode,
            String roomStatusLabel
    ) {
        this.roomId = roomId;
        this.productId = productId;
        this.roomType=roomType;
        this.opponentUserId = opponentUserId;
        this.opponentUserName = opponentUserName != null ? opponentUserName : "";
        this.opponentProfileImageUrl =
                opponentProfileImageUrl != null ? opponentProfileImageUrl : "";

        this.opponentRatingAvg = opponentRatingAvg != null ? opponentRatingAvg : 0.0;
        this.opponentTotalReviews = opponentTotalReviews != null ? opponentTotalReviews : 0;

        this.productName = productName != null ? productName : "";
        this.productPrice = productPrice != null ? productPrice : 0;

        this.roomStatusCode = roomStatusCode != null ? roomStatusCode : "";
        this.roomStatusLabel = roomStatusLabel != null ? roomStatusLabel : "";
    }
}
