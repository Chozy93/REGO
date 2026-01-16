package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

/**
 * 채팅 상세 화면(View) 전용 VO
 * - 채팅 헤더(상품/상대) + 메시지 리스트를 "한 덩어리"로 전달
 * - View에는 VO 1개만 전달 규칙 준수
 */
public class ChatRoomDetailViewVO {

    private final String roomId;

    private final String productId;
    private final String productTitle;
    private final String productThumbnailUrl;

    private final String opponentUserId;
    private final String opponentUserName;

    private final List<ChatMessageItemVO> chatMessageItems;

    public ChatRoomDetailViewVO(
            String roomId,
            String productId,
            String productTitle,
            String productThumbnailUrl,
            String opponentUserId,
            String opponentUserName,
            List<ChatMessageItemVO> chatMessageItems
    ) {
        this.roomId = (roomId != null) ? roomId : "";

        this.productId = (productId != null) ? productId : "";
        this.productTitle = (productTitle != null) ? productTitle : "";
        this.productThumbnailUrl = (productThumbnailUrl != null) ? productThumbnailUrl : "";

        this.opponentUserId = (opponentUserId != null) ? opponentUserId : "";
        this.opponentUserName = (opponentUserName != null) ? opponentUserName : "";

        this.chatMessageItems = (chatMessageItems != null) ? chatMessageItems : Collections.emptyList();
    }

    public String getRoomId() { return roomId; }
    public String getProductId() { return productId; }
    public String getProductTitle() { return productTitle; }
    public String getProductThumbnailUrl() { return productThumbnailUrl; }
    public String getOpponentUserId() { return opponentUserId; }
    public String getOpponentUserName() { return opponentUserName; }
    public List<ChatMessageItemVO> getChatMessageItems() { return chatMessageItems; }
}
