package com.itwillbs.view;

import com.itwillbs.domain.ChatMessageVO;

/**
 * 채팅 메시지 1개 아이템 VO
 * - enum 금지, 날짜 객체 금지, null 금지
 * - isMine 같은 boolean flag는 문서에서 권장 패턴(의미 명확)
 */
public class ChatMessageItemVO {

    private final Long messageId;
    private final Long senderUserId;

    private final String content;

    private final boolean isRead;
    private final String createdAt;   // String만 (포맷은 Service/생성자에서)

    public ChatMessageItemVO(
            Long messageId,
            Long senderUserId,
            String content,
            boolean isRead,
            String createdAt
    ) {
    	this.messageId = messageId;
        this.senderUserId = senderUserId;

        this.content = (content != null) ? content : "";
        this.isRead = isRead;
        this.createdAt = (createdAt != null) ? createdAt : "";
    }
    
    public static ChatMessageItemVO from(ChatMessageVO vo) {
        return new ChatMessageItemVO(
            vo.getMessageId(),
            vo.getSenderId(),
            vo.getContent(),
            vo.isRead(),
            vo.getCreatedAt()
        );
    }

    public Long getMessageId() { return messageId; }
    public Long getSenderUserId() { return senderUserId; }
    public String getContent() { return content; }
    public boolean isRead() { return isRead; }
    public String getCreatedAt() { return createdAt; }
}
