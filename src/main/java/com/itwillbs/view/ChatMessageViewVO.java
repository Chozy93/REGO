package com.itwillbs.view;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageViewVO {

    private Long messageId;

    /** 메시지 본문 */
    private String content;

    /** 내가 보낸 메시지 여부 (UI 정렬/스타일용) */
    private boolean isMine;

    /** 메시지 타입 */
    private String messageTypeCode;   // TEXT, IMAGE
    private String messageTypeLabel;

    /** 읽음 여부 */
    private boolean isRead;

    /** 화면 출력용 시간 문자열 */
    private String createdAtText;

    public ChatMessageViewVO(
            Long messageId,
            String content,
            boolean isMine,
            String messageTypeCode,
            String messageTypeLabel,
            boolean isRead,
            String createdAtText
    ) {
        this.messageId = messageId;
        this.content = content;
        this.isMine = isMine;
        this.messageTypeCode = messageTypeCode;
        this.messageTypeLabel = messageTypeLabel;
        this.isRead = isRead;
        this.createdAtText = createdAtText;
    }
}
