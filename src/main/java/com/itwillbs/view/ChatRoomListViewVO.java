package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅방 리스트 화면(View) 전용 VO
 * - View에는 반드시 VO 1개만 전달 규칙 준수
 * - null 금지: List는 빈 리스트로 초기화
 */
@Getter
@Setter
@ToString
public class ChatRoomListViewVO {

    private final List<ChatRoomListItemVO> chatRoomItems;
    private final Long currentRoomId;
    public ChatRoomListViewVO(List<ChatRoomListItemVO> chatRoomItems,Long currentRoomId) {
        this.chatRoomItems = (chatRoomItems != null) ? chatRoomItems : Collections.emptyList();
        this.currentRoomId = currentRoomId;
    }

    public List<ChatRoomListItemVO> getChatRoomItems() {
        return chatRoomItems;
    }
}
