package com.itwillbs.view.chat;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomHeaderViewVO {

    private final Long roomId;

    /* 채팅 상대 */
    private final Long opponentUserId;
    private final String opponentUserName;
    private final String opponentProfileImageUrl;

    /* 채팅방 상태 */
    private final String roomStatusCode;
    private final String roomStatusLabel;

    public ChatRoomHeaderViewVO(
            Long roomId,
            Long opponentUserId,
            String opponentUserName,
            String opponentProfileImageUrl,
            String roomStatusCode,
            String roomStatusLabel
    ) {
        this.roomId = roomId;
        this.opponentUserId = opponentUserId;
        this.opponentUserName = opponentUserName != null ? opponentUserName : "";
        this.opponentProfileImageUrl = opponentProfileImageUrl != null ? opponentProfileImageUrl : "";
        this.roomStatusCode = roomStatusCode != null ? roomStatusCode : "";
        this.roomStatusLabel = roomStatusLabel != null ? roomStatusLabel : "";
    }
}
