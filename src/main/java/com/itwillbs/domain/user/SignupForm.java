package com.itwillbs.domain.user;

import com.itwillbs.entity.enumtype.Gender;
import lombok.Getter;

@Getter
public class SignupForm {

    private String email;
    private String password;   // 평문
    private String username;
    private String nickname;
    private Gender gender;
}
