package com.itwillbs.entity.enumtype;

public enum ChatFilterType {
    ALL("전체"),
    SELL("판매"),
    BUY("구매");

    private final String label;

    ChatFilterType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
