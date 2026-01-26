package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.itwillbs.domain.user.UserVO;
import com.itwillbs.entity.enumtype.Gender;
import com.itwillbs.entity.enumtype.UserRole;
import com.itwillbs.entity.enumtype.UserStatus; // ✅ 추가

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

    // 🔽 String → Enum 변경
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

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
	    사용자 소개글
	 ========================= */
	 @Column(name = "introduction", length = 1000)
	 private String introduction;

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
    회원가입 전용 생성자
    ========================= */
 public User(
         String email,
         String encodedPassword,
         String username,
         String nickname,
         String phoneNumber,
         String gender
 ) {
     this.email = email;
     this.password = encodedPassword;
     this.username = username;
     this.nickname = nickname;
     this.phoneNumber = phoneNumber;
     // ✅ String → Enum 변환 (도메인 내부 책임)
     this.gender = Gender.valueOf(gender);

     // 기본 상태는 Entity 책임
     this.role = UserRole.USER;
     this.userStatus = UserStatus.ACTIVE;
     this.profileImg = "/images/profile/default.png";
     this.createdAt = LocalDateTime.now();
 }
    
    //변환용 생성자
    public User(UserVO vo) {
        this.userId = vo.getUserId();
        this.email = vo.getEmail();
        this.username = vo.getUsername();
        this.nickname = vo.getNickname();
        this.profileImg = vo.getProfileImg();
        // String → Enum 변환
        this.gender = vo.getGender() != null
                ? Gender.valueOf(vo.getGender())
                : null;
    }	

    
    /* =========================
       상태 변경
    ========================= */
    public void ban() {
        this.userStatus = UserStatus.BANNED;
    }

    public void withdraw() {
        this.userStatus = UserStatus.WITHDRAWN;
    }

    public void changeProfileImage(String profileImg) {
        this.profileImg = profileImg;
    }
    //비밀번호 변경
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}


