package com.itwillbs.entity.enumtype;

public enum ChatRoomType {

    PRODUCT("상품 채팅"),
    ADMIN_DM("관리자 메시지");

    private final String label;

    ChatRoomType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
