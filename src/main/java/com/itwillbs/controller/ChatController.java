package com.itwillbs.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.ChatRoomVO;
import com.itwillbs.entity.ChatRoom;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.ChatService;
import com.itwillbs.view.ChatListViewVO;
import com.itwillbs.view.chat.ChatRoomPageVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    
    @GetMapping("/test/error")
    public String testError() {
        throw new IllegalArgumentException("에러 테스트용 메시지입니다.");
    }
    
    @GetMapping("/list")
    public String chatList(
        @RequestParam(value = "unreadOnly", required = false) Boolean unreadOnly,
        @RequestParam(value = "roomId", required = false) Long roomId, 
        Model model,
        HttpServletRequest request
    ) {
        request.setAttribute("CURRENT_VIEW_NAME", "chat/chat-list");

        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return "redirect:/error-login_required";
        }

        ChatListViewVO chatListItems =
            chatService.getMyChatRooms(unreadOnly, userId);

        model.addAttribute("chatListItems", chatListItems);
        model.addAttribute("initialRoomId", roomId);

        return "chat/chat-list";
    }


    
  
    //채팅방 진입하기
    @GetMapping("/room/{roomId}")
    public String enterChatRoom(
    		 @PathVariable("roomId") Long roomId,
            Model model,
            HttpSession session
    ) {
    	
        Long loginUserId = SecurityUtil.getCurrentUserId();
        System.out.println("채팅방진입 컨트롤러 실행");
        ChatRoomPageVO pageVO =
            chatService.getChatRoomPage(roomId, loginUserId);
        
        
        model.addAttribute("chatRoom", pageVO);
        return "chat/chat-room :: chatRoomContent";
    }
    
    
    /* =========================
       채팅하기 클릭
       - 메시지 없어도 방 생성
       - 실패 시: 기존 화면 유지 + 모달
    ========================= */
    @GetMapping("/start/{productId}")
    public String startChat(
            @PathVariable("productId") Long productId,
            HttpServletRequest request
    ) {
    	System.out.println("🔥🔥🔥 startChat 진입, productId=" + productId);
        
        Long buyerId = SecurityUtil.getCurrentUserId();

        // 이전 페이지 (확장성 핵심)
        String referer = request.getHeader("Referer");

        // 안전장치 (Referer 없을 때)
        if (referer == null || referer.isBlank()) {
            referer = "/";
        }

        // 로그인 안 됐을 경우 → 이전 화면으로
        if (buyerId == null) {
            return "redirect:" + referer;
        }

        ChatRoomVO roomVO = chatService.getOrCreateRoom(productId, buyerId);

        return "redirect:/chat/list?roomId=" + roomVO.getRoomId();
    }
    
    
    @GetMapping("/dm/{userId}")
    public String openAdminDm(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long adminId = loginUser.getUser().getUserId();

        ChatRoom room =
                chatService.getOrCreateAdminDmRoom(adminId, userId);

        // ⚠️ 반드시 chat/list 로 진입
        return "redirect:/chat/list?roomId=" + room.getRoomId();
    }


  
   
}
