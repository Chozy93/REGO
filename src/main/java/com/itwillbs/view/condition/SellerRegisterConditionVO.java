package com.itwillbs.view.condition;

import lombok.Getter;
import lombok.ToString;

/**
 * 판매자 등록 요청 입력용 Condition VO
 *
 * - Controller 계층 전용
 * - View(Form) 바인딩 목적
 * - Domain / Entity와 분리
 */
@Getter
@ToString
public class SellerRegisterConditionVO {

    /* =========================
       판매자 소개글
    ========================= */
    private String description;
}
