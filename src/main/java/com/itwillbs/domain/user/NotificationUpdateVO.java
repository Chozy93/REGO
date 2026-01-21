package com.itwillbs.domain.user;

import lombok.Data;

@Data 
public class NotificationUpdateVO {
    private String type;    // 어떤 알림인지 (chat, activity, marketing 등)
    private boolean status; // 켜졌는지(true) 꺼졌는지(false)
}
