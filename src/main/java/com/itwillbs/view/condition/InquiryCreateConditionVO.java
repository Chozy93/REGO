package com.itwillbs.view.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryCreateConditionVO {

    /* =========================
       문의 유형 (외부 입력)
    ========================= */
    private String inquiryType;   // ACCOUNT, PAYMENT, ...

    /* =========================
       문의 내용
    ========================= */
    private String title;
    private String content;

    /* =========================
       연관 대상 (조건부)
    ========================= */
    private Long orderId;          // PAYMENT

}