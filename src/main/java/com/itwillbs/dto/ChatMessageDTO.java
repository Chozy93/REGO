package com.itwillbs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ChatMessageDTO {

    private Long messageId;
    private Long roomId;

    private Long senderUserId;
    private String content;

    /* 메시지 타입 */
    private String messageTypeCode;   // TEXT, IMAGE
    private String messageTypeLabel;

    /* 읽음 여부 */
    private Boolean isRead;

    /* 생성 시간 */
    private LocalDateTime createdAt;
}
