package com.itwillbs.domain.user;

import com.itwillbs.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserVO {

    private final Long userId;
    private final String email;
    private String username;
    private String nickname;
    private String profileImg;
    private String gender;   // ✅ String
    
    /* =========================
    Entity → VO 생성자
 ========================= */
 public UserVO(User entity) {
     this.userId = entity.getUserId();
     this.email = entity.getEmail();
     this.username = entity.getUsername();
     this.nickname = entity.getNickname();
     this.profileImg = entity.getProfileImg();
     this.gender = entity.getGender() != null
             ? entity.getGender().name()
             : null;
 }
}
