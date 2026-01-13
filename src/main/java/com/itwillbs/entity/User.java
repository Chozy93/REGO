package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.itwillbs.entity.enumtype.Gender;
import com.itwillbs.entity.enumtype.UserRole;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_nickname", columnNames = "nickname"),
        @UniqueConstraint(name = "uk_users_phone", columnNames = "phone_number")
    }
)
@Getter
@Setter
public class User {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /* =========================
       인증 / 계정 정보
    ========================= */
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "user_status", length = 255)
    private String userStatus;

    /* =========================
       사용자 정보
    ========================= */
    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "nickname", length = 50, nullable = false)
    private String nickname;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "profile_img", length = 500, nullable = false)
    private String profileImg;

    /* =========================
       생성 정보
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected User() {}

   
    /* =========================
       상태 변경
    ========================= */
    public void ban() {
        this.userStatus = "BANNED";
    }

    public void withdraw() {
        this.userStatus = "WITHDRAWN";
    }

    public void changeProfileImage(String profileImg) {
        this.profileImg = profileImg;
    }
    
    public User(String email, String password, String username, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = UserRole.USER; // 기본값
        this.userStatus = "ACTIVE"; // 기본값
        this.profileImg = "default.png"; // 필수값이라 일단 세팅
        this.createdAt = LocalDateTime.now();
    }
}


