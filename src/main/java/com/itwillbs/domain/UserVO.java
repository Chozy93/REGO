package com.itwillbs.domain;


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
    private final String username;
    private final String nickname;
    private final String profileImg;

    /* =========================
       Entity → VO (조회)
    ========================= */
    public UserVO(User entity) {
        this.userId = entity.getUserId();
        this.username = entity.getUsername();
        this.nickname = entity.getNickname();
        this.profileImg = entity.getProfileImg();
    }
}
