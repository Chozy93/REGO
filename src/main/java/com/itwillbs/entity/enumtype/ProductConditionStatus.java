package com.itwillbs.entity.enumtype;

/**
 * 상품 상태 (상품 손상/사용 정도)
 */
public enum ProductConditionStatus {

    NEW("새 상품"),
    LIKE_NEW("거의 새 것"),
    GOOD("사용감 있음"),
    FAIR("사용감 많음");

    private final String label;

    ProductConditionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
