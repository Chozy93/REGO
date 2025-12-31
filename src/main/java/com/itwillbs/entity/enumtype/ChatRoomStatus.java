package com.itwillbs.entity.enumtype;

public enum ChatRoomStatus {

    ACTIVE("진행중"),
    CLOSED("종료됨");

    private final String label;

    ChatRoomStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}