package com.itwillbs.entity.enumtype;


import lombok.Getter;

@Getter
public enum FaqCategory {

    MEMBER_ACCOUNT("회원/계정"),
    PURCHASE_SALE("구매/판매"),
    PAYMENT_REFUND("결제/환불"),
    DELIVERY("배송");

    private final String label;

    FaqCategory(String label) {
        this.label = label;
    }

    /* =========================
       code → enum 변환
    ========================= */
    public static FaqCategory fromCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("FAQ 카테고리 코드는 필수입니다.");
        }

        try {
            return FaqCategory.valueOf(code);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 FAQ 카테고리 코드: " + code);
        }
    }
}
