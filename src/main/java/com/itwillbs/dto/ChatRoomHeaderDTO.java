package com.itwillbs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoomHeaderDTO {

    /* 채팅방 */
    private Long roomId;

    /* 상대방 */
    private Long opponentUserId;
    private String opponentUserNickName;
    private String opponentProfileImg;

    /* 채팅방 상태 */
    private String roomStatusCode;   // 예: ACTIVE, CLOSED
    private String roomStatusLabel;  // 예: 진행중, 종료됨
}
