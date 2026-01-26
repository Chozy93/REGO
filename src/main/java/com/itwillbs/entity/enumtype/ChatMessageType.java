package com.itwillbs.entity.enumtype;

import lombok.Getter;

@Getter
public enum ChatMessageType {

    TEXT("일반 메시지"),
    SYSTEM("시스템 메시지"),
    IMAGE("이미지 메시지");

    private final String label;

    ChatMessageType(String label) {
        this.label = label;
    }
}