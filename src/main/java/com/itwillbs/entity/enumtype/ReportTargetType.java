package com.itwillbs.entity.enumtype;

public enum ReportTargetType {

    USER("회원"),
    PRODUCT("상품"),
    CHAT("채팅");

    private final String label;

    ReportTargetType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
