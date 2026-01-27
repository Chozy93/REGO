package com.itwillbs.view.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SellerProductRegisterConditionVO {

    /* =========================
       기본 정보
    ========================= */
    private String title;
    private String description;
    private Integer price;

    /* =========================
       상태 / 거래 방식 (문자열)
    ========================= */
    private String conditionStatus; // NEW, LIKE_NEW, GOOD, FAIR
    private String tradeType;        // DIRECT, DELIVERY, ALL

    /* =========================
       카테고리
    ========================= */
    private Long categoryId;

    /* =========================
       지역 (Product 테이블 그대로)
    ========================= */
    private String regionSidoCode;
    private String regionSigunguCode;
    private String regionEupmyeondongCode;
    private String regionDisplayName;
}
