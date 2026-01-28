package com.itwillbs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoomHeaderDTO {

    /* =========================
       채팅방
    ========================= */
    private Long roomId;
    private Long productId;

    /* =========================
       상대방 기본 정보
    ========================= */
    private Long opponentUserId;
    private String opponentUserNickName;
    private String opponentProfileImg;

    /* =========================
       상대방 신뢰도 (seller_profile 기반)
    ========================= */
    private double opponentRatingAvg;     // ★ 평점 평균 (없으면 0.0)
    private int opponentTotalReviews;      // 후기 수 (없으면 0)

    /* =========================
       상품 정보
    ========================= */
    private String productName;
    private int productPrice;

    /* =========================
       채팅방 상태
    ========================= */
    private String roomStatusCode;   // ACTIVE, CLOSED
    private String roomStatusLabel;  // 진행중, 종료됨
}
