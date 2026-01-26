package com.itwillbs.view;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatListViewVO {

    /**
     * 채팅방 목록 (화면에서 th:each로 반복 렌더링)
     */
    private final List<ChatListItemVO> items;

    public ChatListViewVO(List<ChatListItemVO> items) {
        this.items = items;
    }
}
