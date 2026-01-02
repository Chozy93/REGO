package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.ChatMessageVO;
import com.itwillbs.domain.chat.ChatRoomListItemVO;
import com.itwillbs.domain.chat.ChatRoomListViewVO;
import com.itwillbs.entity.enumtype.ChatFilterType;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
	
    @GetMapping("/rooms")
    public String chatList(
    		 @RequestParam(name = "type", defaultValue = "ALL") ChatFilterType type,
    		    @RequestParam(name = "unread", required = false) Boolean unread,
    		    Model model
    ) {

        /* =========================
           1. 필터 데이터
           (이건 View 상태 제어용이므로 별도 OK)
        ========================== */
        model.addAttribute("chatFilters", ChatFilterType.values());
        model.addAttribute("currentFilter", type);
        model.addAttribute("unreadOnly", unread != null && unread);

        /* =========================
           2. 채팅방 아이템 더미 생성
        ========================== */
        List<ChatRoomListItemVO> items = new ArrayList<>();

        items.add(new ChatRoomListItemVO(
                "room-1001",
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
                "room-1002",
                "product-502",
                "아이패드 미니 6세대",
                "https://example.com/thumb2.jpg",
                "user-2003",
                "이영희",
                "가격 조정 가능할까요?",
                "2025-01-02 18:42",
                0
        ));

        /* =========================
           3. View 전용 VO로 감싸서 전달
        ========================== */
        ChatRoomListViewVO viewVO = new ChatRoomListViewVO(items);
        model.addAttribute("view", viewVO);

        return "chat/list";
    }

	
    @MessageMapping("/chat.send")
    @SendTo("/topic/chat/1")
    public ChatMessageVO send(ChatMessageVO message) {
        return message;
    }
}