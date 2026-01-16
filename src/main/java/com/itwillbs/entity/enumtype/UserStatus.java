package com.itwillbs.entity.enumtype;

public enum UserStatus {

    ACTIVE("정상"),
    BANNED("정지"),
    WITHDRAWN("탈퇴");

    private final String label;

    UserStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
