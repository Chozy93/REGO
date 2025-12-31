package com.itwillbs.domain.chat;

/**
 * 채팅방 리스트의 "1개 채팅방" 아이템 VO
 * - enum 금지, 날짜 객체 금지, null 금지
 */
public class ChatRoomListItemVO {

    private final String roomId;                 // VO에서는 String 허용(문서 ID 규칙)
    private final String productId;
    private final String productTitle;
    private final String productThumbnailUrl;

    private final String opponentUserId;
    private final String opponentUserName;

    private final String lastMessageContent;
    private final String lastMessageCreatedAt;   // yyyy-MM-dd HH:mm 등 "문자열"로만

    private final int unreadCount;

    public ChatRoomListItemVO(
            String roomId,
            String productId,
            String productTitle,
            String productThumbnailUrl,
            String opponentUserId,
            String opponentUserName,
            String lastMessageContent,
            String lastMessageCreatedAt,
            Integer unreadCount
    ) {
        this.roomId = (roomId != null) ? roomId : "";
        this.productId = (productId != null) ? productId : "";
        this.productTitle = (productTitle != null) ? productTitle : "";
        this.productThumbnailUrl = (productThumbnailUrl != null) ? productThumbnailUrl : "";

        this.opponentUserId = (opponentUserId != null) ? opponentUserId : "";
        this.opponentUserName = (opponentUserName != null) ? opponentUserName : "";

        this.lastMessageContent = (lastMessageContent != null) ? lastMessageContent : "";
        this.lastMessageCreatedAt = (lastMessageCreatedAt != null) ? lastMessageCreatedAt : "";

        this.unreadCount = (unreadCount != null) ? unreadCount : 0;
    }

    public String getRoomId() { return roomId; }
    public String getProductId() { return productId; }
    public String getProductTitle() { return productTitle; }
    public String getProductThumbnailUrl() { return productThumbnailUrl; }
    public String getOpponentUserId() { return opponentUserId; }
    public String getOpponentUserName() { return opponentUserName; }
    public String getLastMessageContent() { return lastMessageContent; }
    public String getLastMessageCreatedAt() { return lastMessageCreatedAt; }
    public int getUnreadCount() { return unreadCount; }
}
