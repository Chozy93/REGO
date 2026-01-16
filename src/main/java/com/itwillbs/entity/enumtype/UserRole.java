package com.itwillbs.entity.enumtype;

public enum UserRole {

    USER("일반회원"),
    ADMIN("관리자");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
