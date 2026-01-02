package com.itwillbs.view;

/**
 * 채팅 메시지 1개 아이템 VO
 * - enum 금지, 날짜 객체 금지, null 금지
 * - isMine 같은 boolean flag는 문서에서 권장 패턴(의미 명확)
 */
public class ChatMessageItemVO {

    private final String messageId;
    private final String senderUserId;

    private final boolean isMine;
    private final String content;

    private final boolean isRead;
    private final String createdAt;   // String만 (포맷은 Service/생성자에서)

    public ChatMessageItemVO(
            String messageId,
            String senderUserId,
            boolean isMine,
            String content,
            boolean isRead,
            String createdAt
    ) {
        this.messageId = (messageId != null) ? messageId : "";
        this.senderUserId = (senderUserId != null) ? senderUserId : "";
        this.isMine = isMine;

        this.content = (content != null) ? content : "";
        this.isRead = isRead;
        this.createdAt = (createdAt != null) ? createdAt : "";
    }

    public String getMessageId() { return messageId; }
    public String getSenderUserId() { return senderUserId; }
    public boolean isMine() { return isMine; }
    public String getContent() { return content; }
    public boolean isRead() { return isRead; }
    public String getCreatedAt() { return createdAt; }
}
