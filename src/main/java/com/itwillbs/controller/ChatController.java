package com.itwillbs.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.domain.ChatMessageVO;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
    @GetMapping("/rooms")
    public String chatPage() {
        return "chat/list"; // templates/chat/chat.html
    }
	
    @MessageMapping("/chat.send")
    @SendTo("/topic/chat/1")
    public ChatMessageVO send(ChatMessageVO message) {
        return message;
    }
}