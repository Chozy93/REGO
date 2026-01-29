package com.itwillbs.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryType {

    ACCOUNT("계정 / 로그인"),
    PAYMENT("결제 / 주문"),
    SYSTEM("시스템 / 오류"),
    PRODUCT("상품 관련"),
    DELIVERY("배송"),
    ETC("기타");

    private final String label;

    public static InquiryType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("문의 유형이 비어있습니다.");
        }

        try {
            return InquiryType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 문의 유형입니다: " + value);
        }
    }
}
