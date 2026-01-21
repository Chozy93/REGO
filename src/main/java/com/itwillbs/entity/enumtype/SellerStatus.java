package com.itwillbs.entity.enumtype;

public enum SellerStatus {

    ACTIVE("판매 가능"),
    SUSPENDED("판매 중지");

    private final String label;

    SellerStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
