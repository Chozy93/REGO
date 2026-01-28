package com.itwillbs.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MyOrderSelectViewVO {

    /* =========================
       주문 식별
    ========================= */
    private Long orderId;

    /* =========================
       상품 정보
    ========================= */
    private String productTitle;

    /* =========================
       결제 정보
    ========================= */
    private Long totalAmount;   // 결제 금액
    private String dealType;    // DIRECT / DELIVERY

    /* =========================
       나의 역할
    ========================= */
    private String myRole;      // BUYER / SELLER

    /* =========================
       주문 생성일
    ========================= */
    private LocalDateTime createdAt;
}
