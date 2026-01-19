package com.itwillbs.view;

import java.util.List;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomPageVO {

    private final ChatRoomHeaderViewVO header;
    private final List<ChatMessageViewVO> messages;

    public ChatRoomPageVO(
            ChatRoomHeaderViewVO header,
            List<ChatMessageViewVO> messages
    ) {
        this.header = header;
        this.messages = messages != null ? messages : List.of();
    }
}