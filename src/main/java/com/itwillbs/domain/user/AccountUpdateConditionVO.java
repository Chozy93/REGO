package com.itwillbs.domain.user;

import lombok.Getter;

/**
 * 계정 정보 수정 입력 조건 객체
 * View → Controller → Service 전달 전용
 */
@Getter
public class AccountUpdateConditionVO {

    private String email;
    private String phoneNumber;
}
