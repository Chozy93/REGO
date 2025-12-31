package com.itwillbs.entity.enumtype;

/**
 * 판매 상태 (거래 진행 상태)
 */
public enum ProductSalesStatus {

    ON_SALE("판매중"),
    RESERVED("예약중"),
    SOLD("판매완료"),
    HIDDEN("숨김");

    private final String label;

    ProductSalesStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
