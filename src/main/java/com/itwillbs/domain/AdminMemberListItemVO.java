package com.itwillbs.domain;

import java.time.format.DateTimeFormatter;

import com.itwillbs.dto.AdminMemberListDTO;

public class AdminMemberListItemVO {

    private String userId;
    private String email;
    private String role;
    private String status;
    private String username;
    private String nickname;
    private String phoneNumber;
    private String createdAt;

    public AdminMemberListItemVO(AdminMemberListDTO dto) {
        this.userId = String.valueOf(dto.getUserId());
        this.email = dto.getEmail();
        this.role = dto.getRole();
        this.status = dto.getUserStatus();
        this.username = dto.getUsername();
        this.nickname = dto.getNickname();
        this.phoneNumber = dto.getPhoneNumber();
        this.createdAt = dto.getCreatedAt()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getUsername() { return username; }
    public String getNickname() { return nickname; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getCreatedAt() { return createdAt; }
}
