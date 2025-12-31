package com.itwillbs.domain.chat;

import java.util.Collections;
import java.util.List;

/**
 * 채팅방 리스트 화면(View) 전용 VO
 * - View에는 반드시 VO 1개만 전달 규칙 준수
 * - null 금지: List는 빈 리스트로 초기화
 */
public class ChatRoomListViewVO {

    private final List<ChatRoomListItemVO> chatRoomItems;

    public ChatRoomListViewVO(List<ChatRoomListItemVO> chatRoomItems) {
        this.chatRoomItems = (chatRoomItems != null) ? chatRoomItems : Collections.emptyList();
    }

    public List<ChatRoomListItemVO> getChatRoomItems() {
        return chatRoomItems;
    }
}
