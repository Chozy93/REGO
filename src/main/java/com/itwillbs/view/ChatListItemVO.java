package com.itwillbs.view;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatListItemVO {

    private Long roomId;

    private Long opponentUserId;
    private String opponentNickName;
    private String opponentProfileImg;

    private String lastMessage;

    /* 화면 출력용 */
    private String lastSentAtText; // 예: 방금 전, 3분 전, 2026.01.16

    private int unreadCount;

    
    public ChatListItemVO(
            Long roomId,
            Long opponentUserId,
            String opponentNickName,
            String opponentProfileImg,
            String lastMessage,
            String lastSentAtText,
            int unreadCount
    ) {
        this.roomId = roomId;
        this.opponentUserId = opponentUserId;
        this.opponentNickName = opponentNickName;
        this.opponentProfileImg = opponentProfileImg;
        this.lastMessage = lastMessage;
        this.lastSentAtText = lastSentAtText;
        this.unreadCount = unreadCount;
    }
}
