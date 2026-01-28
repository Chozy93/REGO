package com.itwillbs.dto;

import java.time.LocalDateTime;

public class AdminMemberListDTO {

    private Long userId;
    private String email;
    private String role;
    private String userStatus;
    private String username;
    private String nickname;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getUserStatus() { return userStatus; }
    public String getUsername() { return username; }
    public String getNickname() { return nickname; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
