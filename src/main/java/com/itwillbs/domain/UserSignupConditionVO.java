package com.itwillbs.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserSignupConditionVO {
    // 회원가입 필수 정보
    private String email;
    private String password;
    private String username;
    private String nickname;
    private String phoneNumber;

    private String authCode;      // 사용자가 입력한 인증번호
}