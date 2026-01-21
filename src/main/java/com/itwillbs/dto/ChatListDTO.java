package com.itwillbs.dto;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString

public class ChatListDTO {
	
	
    private Long roomId;

    private Long opponentUserId;
    private String opponentNickName;
    private String opponentProfileImg;

    private String lastMessage;
    private LocalDateTime lastSentAt;

    private int unreadCount;
}
