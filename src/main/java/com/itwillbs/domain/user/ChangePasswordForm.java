package com.itwillbs.domain.user;

import lombok.Getter;

@Getter
public class ChangePasswordForm {

    private String currentPassword;
    private String newPassword; // 탈퇴 시 null 허용
}
