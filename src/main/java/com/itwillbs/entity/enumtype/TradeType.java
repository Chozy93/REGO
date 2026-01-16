package com.itwillbs.entity.enumtype;

/**
 * 거래 방식
 */
public enum TradeType {

    DIRECT("직거래"),
    DELIVERY("택배"),
    ALL("직거래 + 택배");

    private final String label;

    TradeType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
