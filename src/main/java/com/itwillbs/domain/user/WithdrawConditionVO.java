package com.itwillbs.domain.user;

import lombok.Getter;

/**
 * 회원 탈퇴 입력 조건 객체
 * 본인 확인용 비밀번호 전달 전용
 */
@Getter
public class WithdrawConditionVO {

    private String password;   // 본인 확인용 (평문)
}
