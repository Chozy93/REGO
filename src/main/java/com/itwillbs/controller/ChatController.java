package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.ChatMessageVO;
import com.itwillbs.domain.ChatRoomVO;
import com.itwillbs.domain.user.UserVO;
import com.itwillbs.entity.enumtype.ChatFilterType;
import com.itwillbs.view.ChatRoomListItemVO;
import com.itwillbs.view.ChatRoomListViewVO;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
	
	
	@GetMapping({"/list", "/list/{roomId}"})
	public String chatList(
	    @PathVariable(name = "roomId", required = false) Long roomId,
	    @RequestParam(name = "type", defaultValue = "ALL") ChatFilterType type,
	    @RequestParam(name = "unread", required = false) Boolean unread,
	    Model model
	){

	    /* =========================
	       1. 필터 상태
	    ========================= */
	    model.addAttribute("chatFilters", ChatFilterType.values());
	    model.addAttribute("currentFilter", type);
	    model.addAttribute("unreadOnly", unread != null && unread);

	    /* =========================
	       2. 채팅 리스트 (더미 유지)
	    ========================= */
	    List<ChatRoomListItemVO> items = new ArrayList<>();

	    items.add(new ChatRoomListItemVO(
	            "1001",
	            "product-501",
	            "오프화이트 후드 판매합니다",
	            "https://example.com/thumb1.jpg",
	            "user-2002",
	            "김철수",
	            "네, 오늘 저녁에 가능합니다.",
	            "2025-01-03 21:14",
	            2
	    ));

	    items.add(new ChatRoomListItemVO(
	            "1002",
	            "product-502",
	            "아이패드 미니 6세대",
	            "https://example.com/thumb2.jpg",
	            "user-2003",
	            "이영희",
	            "가격 조정 가능할까요?",
	            "2025-01-02 18:42",
	            0
	    ));

	    ChatRoomListViewVO viewVO = new ChatRoomListViewVO(items,roomId);
	    model.addAttribute("view", viewVO);

	    /* =========================
	       3. 채팅방 선택 시 (우측 fragment)
	    ========================= */
	    if (roomId != null) {

	        // 로그인 유저 (임시 더미)
	        Long loginUserId = 1L;
	        model.addAttribute("loginUserId", loginUserId);

	     // 상대 유저 더미
	        UserVO partnerUser = new UserVO(
	                2L,                         // userId
	                "kim2323@test.com",         // email (아이디)
	                "김철수",                   // username
	                "철수",                     // nickname
	                "/img/dummy/avatar.png",    // profileImg
	                null                        // gender (더미라 null)
	        );

	        model.addAttribute("partnerUser", partnerUser);

	        // 채팅방 더미
	        ChatRoomVO chatRoomVO = new ChatRoomVO(); // createdAt만 쓸 거면 생성자 보완 가능
	        model.addAttribute("chatRoomVO", chatRoomVO);

	        // 메시지 더미
	        List<ChatMessageVO> messages = new ArrayList<>();
	        messages.add(new ChatMessageVO("안녕하세요! 판매중인가요?"));
	        messages.add(new ChatMessageVO("네 아직 판매중입니다."));
	        messages.add(new ChatMessageVO("그럼 오늘 거래 가능할까요?"));
	        messages.add(new ChatMessageVO("그럼 오늘 거래 가능할까요?"));
	        messages.add(new ChatMessageVO("그럼 오늘 거래 가능할까요?"));
	        messages.add(new ChatMessageVO("그럼 오늘 거래 가능할까요?"));
	        messages.add(new ChatMessageVO("그럼 오늘 거래 가능할까요?"));
	        messages.add(new ChatMessageVO("그럼 오늘 거래 가능할까요?"));
	        messages.add(new ChatMessageVO("그럼 오늘 거래 가능할까요?"));

	        model.addAttribute("messages", messages);
	    }

	    return "chat/chat-list";
	}
    
 
	
    @MessageMapping("/chat.send")
    @SendTo("/topic/chat/1")
    public ChatMessageVO send(ChatMessageVO message) {
        return message;
    }
}