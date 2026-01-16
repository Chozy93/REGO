package com.itwillbs.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductPriceHistoryVO {
	 /* =========================
    PK
   ========================= */
   private Long historyId;

   /* =========================
      연관 정보
   ========================= */
   private Long productId;
   private Long changedBy; // users.user_id

   /* =========================
      가격 정보
   ========================= */
   private int oldPrice;
   private int newPrice;

   /* =========================
      변경 시각
   ========================= */
   private LocalDateTime changedAt;
}
