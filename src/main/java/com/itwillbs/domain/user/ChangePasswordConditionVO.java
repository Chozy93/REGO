package com.itwillbs.domain.user;

import lombok.Getter;

/**
 * 비밀번호 변경 입력 조건 객체
 * View → Controller → Service 전달 전용
 */
@Getter
public class ChangePasswordConditionVO {

    private String currentPassword;
    private String newPassword; // 탈퇴 시 null 허용
}
