package com.itwillbs.websocket;


import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.itwillbs.service.ChatService;
import com.itwillbs.view.chat.ChatMessageItemVO;
import com.itwillbs.view.condition.ChatMessageSendConditionVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatSessionRegistry chatSessionRegistry;
    /* =========================
    메시지 전송 (TEXT)
    - 입력 수집 전용
    - 상태/타입 판단 ❌
 ========================= */
 @MessageMapping("/chat/{roomId}/send")
 public void sendMessage(  @DestinationVariable("roomId") Long roomId,
                         ChatMessageSendConditionVO condition, SimpMessageHeaderAccessor accessor) {


	    // 1. 입력자 식별 (HandshakeInterceptor에서 저장한 userId)
	 Map<String, Object> attrs = accessor.getSessionAttributes();
	 if (attrs == null || attrs.get("userId") == null) {
	     throw new IllegalStateException("WebSocket userId not found in session");
	 }
	 Long senderId = (Long) attrs.get("userId");

	    if (senderId == null) {
	        throw new IllegalStateException("WebSocket userId not found in session");
	    }

     // 2. 서비스 호출 (비즈니스 위임)
     ChatMessageItemVO messageVO =
             chatService.sendTextMessage(roomId, senderId, condition);
     
     
	     /* =========================
	     🔥 여기! 여기서 read 처리 판단
	     ========================= */
	
	  // 2-1. 수신자 계산 (서비스 메서드로 분리 권장)
	  Long receiverId =
	          chatService.resolveReceiverId(roomId, senderId);
	
	  // 2-2. 수신자가 "채팅방을 실제 보고 있으면" read 처리
	  if (chatSessionRegistry.isViewingRoom(roomId, receiverId)) {
	      chatService.markMessageAsRead(messageVO.getMessageId());
	  }

     
     
     // 3. 브로드캐스트 (출력 전용 VO)
     messagingTemplate.convertAndSend(
             "/topic/chat." + roomId,
             messageVO
     );
 }
 
 
	 @MessageMapping("/chat/{roomId}/view")
	 public void enterRoom(@DestinationVariable("roomId") Long roomId,
	                       SimpMessageHeaderAccessor accessor) {
	
	     Map<String, Object> attrs = accessor.getSessionAttributes();
	     if (attrs == null || attrs.get("userId") == null) {
	         throw new IllegalStateException("WebSocket userId not found in session");
	     }
	
	     Long userId = (Long) attrs.get("userId");
	     chatSessionRegistry.markViewing(roomId, userId);
	 }
	
	 
	 @MessageMapping("/chat/{roomId}/leave")
	 public void leaveRoom(@DestinationVariable("roomId") Long roomId,
	                       SimpMessageHeaderAccessor accessor) {
	
	     Map<String, Object> attrs = accessor.getSessionAttributes();
	     if (attrs == null || attrs.get("userId") == null) {
	         return;
	     }
	
	     Long userId = (Long) attrs.get("userId");
	     chatSessionRegistry.unmarkViewing(roomId, userId);
	 }

 
 
}
