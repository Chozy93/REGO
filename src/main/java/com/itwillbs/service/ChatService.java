package com.itwillbs.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.ChatMessageVO;
import com.itwillbs.domain.ChatRoomVO;
import com.itwillbs.dto.ChatListDTO;
import com.itwillbs.dto.ChatMessageDTO;
import com.itwillbs.dto.ChatRoomHeaderDTO;
import com.itwillbs.entity.ChatMessage;
import com.itwillbs.entity.ChatRoom;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.ChatMapper;
import com.itwillbs.repository.ChatMessageRepository;
import com.itwillbs.repository.ChatRoomRepository;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.view.ChatListItemVO;
import com.itwillbs.view.ChatListViewVO;
import com.itwillbs.view.chat.ChatMessageItemVO;
import com.itwillbs.view.chat.ChatMessageViewVO;
import com.itwillbs.view.chat.ChatRoomHeaderViewVO;
import com.itwillbs.view.chat.ChatRoomPageVO;
import com.itwillbs.view.condition.ChatMessageSendConditionVO;
import com.itwillbs.websocket.ChatSessionRegistry;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;
    private final ChatSessionRegistry chatSessionRegistry;
    
    
    
    //새태칭 알림
    @Transactional(readOnly = true)
    public boolean hasUnreadChat(Long userId) {
        return chatMapper.existsUnreadChat(userId);
    }
    
    /* =========================
    	내 채팅방 리스트 조회
 	========================= */
    public ChatListViewVO getMyChatRooms(Boolean unreadOnly,Long userId) {

       
        System.out.println("[ChatService] getMyChatRooms() 호출");
        System.out.println("[ChatService] login userId = " + userId);
        System.out.println("[ChatService] unreadOnly = " + unreadOnly);

      

        // 1) Mapper 조회 (DTO)
        List<ChatListDTO> chatListItems =
                chatMapper.selectMyChatList(userId, unreadOnly);

        System.out.println("[ChatService] 조회된 채팅방 수 = " + chatListItems.size());

        for (ChatListDTO dto : chatListItems) {
            System.out.println(
                "[ChatService] roomId=" + dto.getRoomId()
              + ", opponent=" + dto.getOpponentNickName()
              + ", unread=" + dto.getUnreadCount()
            );
        }

        // 2) DTO → View 변환 (VO)
        List<ChatListItemVO> items = chatListItems.stream()
                .map(dto -> new ChatListItemVO(
                        dto.getRoomId(),
                        dto.getOpponentUserId(),
                        dto.getOpponentNickName(),
                        dto.getOpponentProfileImg(),
                        dto.getLastMessage(),
                        formatChatListTime(dto.getLastSentAt()),
                        dto.getUnreadCount()
                ))
                .toList();
        
      
        return new ChatListViewVO(items);
    }

    
    /* =========================
       채팅방 조회 또는 생성
       (상품 + 구매자 기준)
    ========================= */
    @Transactional
    public ChatRoomVO getOrCreateRoom(Long productId, Long buyerId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new EntityNotFoundException("구매자가 존재하지 않습니다."));

        User seller = product.getSeller();

        ChatRoom room = chatRoomRepository
            .findByProductAndBuyer(product, buyer)
            .orElseGet(() -> chatRoomRepository.save(
                new ChatRoom(product, buyer, seller)
            ));

        return room.toVO();
    }


    /* =========================
    메시지 전송 (TEXT)
    - Command Service
    - ConditionVO → Entity → 저장 → View VO
 ========================= */
 @Transactional
 public ChatMessageItemVO sendTextMessage(
         Long roomId,
         Long senderId,
         ChatMessageSendConditionVO condition
 ) {

     /* 1. 채팅방 조회 */
     ChatRoom chatRoom = chatRoomRepository.findById(roomId)
             .orElseThrow(() ->
                     new EntityNotFoundException("채팅방이 존재하지 않습니다.")
             );

     /* 2. 발신자 조회 */
     User sender = userRepository.findById(senderId)
             .orElseThrow(() ->
                     new EntityNotFoundException("발신자가 존재하지 않습니다.")
             );

     /* 3. 메시지 Entity 생성
        - messageType = TEXT
        - isRead = false
        - createdAt = now
        (전부 Entity 내부 책임)
     */
     ChatMessage message =
             new ChatMessage(chatRoom, sender, condition);

     /* 4. 저장 */
     ChatMessage savedMessage =
             chatMessageRepository.save(message);
   
     // 엔터티 vo 변환
     ChatMessageVO domainVO = new ChatMessageVO(savedMessage);
     /* 5. vo-> View VO 변환 후 반환 */
     return ChatMessageItemVO.from(domainVO);
 }

 	//읽음처리
	 @Transactional
	 public void markMessageAsRead(Long messageId) {
	     ChatMessage message = chatMessageRepository.findById(messageId)
	         .orElseThrow();
	
	     message.markAsRead();
	 }
	 
	 //수신자 체크
	 @Transactional(readOnly = true)
	 public Long resolveReceiverId(Long roomId, Long senderId) {

	     ChatRoom chatRoom = chatRoomRepository.findById(roomId)
	             .orElseThrow(() ->
	                     new EntityNotFoundException("채팅방이 존재하지 않습니다.")
	             );

	     if (chatRoom.getBuyer().getUserId().equals(senderId)) {
	         return chatRoom.getSeller().getUserId();
	     }

	     if (chatRoom.getSeller().getUserId().equals(senderId)) {
	         return chatRoom.getBuyer().getUserId();
	     }

	     throw new IllegalStateException("채팅방 참여자가 아닙니다.");
	 }
    
	 
	 
    //채팅방 진입
    public ChatRoomPageVO getChatRoomPage(Long roomId,Long loginUserId) {
    	
    	 System.out.println("채팅방진입 서비스 실행");
        // 1. 채팅방 + 상대방 조회
        ChatRoomHeaderDTO headerDTO =
            chatMapper.selectChatRoomHeader(roomId, loginUserId);

        if (headerDTO == null) {
            throw new EntityNotFoundException("채팅방이 존재하지 않습니다.");
        }
        //메세지 읽음 처리
        int updatedCount =
                chatMapper.markMessagesAsRead(roomId, loginUserId);
        // 방에 메시지가 있는데 0이하면 이상 상황
        if (updatedCount < 0) {
            throw new IllegalStateException("채팅 메시지 읽음 처리 실패");
        }
     // 방에 메시지가 있는데 0이하면 이상 상황
        if (updatedCount < 0) {
            throw new IllegalStateException("채팅 메시지 읽음 처리 실패");
        }

  

        
        
        // 2. 메시지 목록 조회
        List<ChatMessageDTO> messageDTOs =
            chatMapper.selectMessagesByRoomId(roomId);
        if (messageDTOs == null) {
            throw new IllegalStateException("채팅 메시지 조회 실패");
        }
        // 3. DTO → View VO 변환
        ChatRoomHeaderViewVO headerVO = new ChatRoomHeaderViewVO(
            headerDTO.getRoomId(),
            headerDTO.getOpponentUserId(),
            headerDTO.getOpponentUserNickName(),
            headerDTO.getOpponentProfileImg(),
            headerDTO.getRoomStatusCode(),
            headerDTO.getRoomStatusLabel()
        );

        List<ChatMessageViewVO> messageVOs =
            messageDTOs.stream()
                .map(dto -> new ChatMessageViewVO(
                    dto.getMessageId(),
                    dto.getContent(),
                    dto.getSenderUserId().equals(loginUserId),
                    dto.getMessageTypeCode(),
                    dto.getMessageTypeLabel(),
                    Boolean.TRUE.equals(dto.getIsRead()),
                    formatChatListTime(dto.getCreatedAt())
                ))
                .toList();
        System.out.println("메세지 상대방 나 체크 : "+messageVOs);
        return new ChatRoomPageVO(headerVO, messageVOs);
    }
    
    
    
    //datetime string 변환 메서드
    private String formatChatListTime(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
