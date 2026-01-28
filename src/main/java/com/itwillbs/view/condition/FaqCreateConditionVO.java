package com.itwillbs.view.condition;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FaqCreateConditionVO {

    /* =========================
       질문 / 답변
    ========================= */
    private final String question;
    private final String answer;

    /* =========================
       카테고리 (enum code)
    ========================= */
    private final String faqCategoryCode;

    /* =========================
       활성 여부
    ========================= */
    private final boolean isActive;

    public FaqCreateConditionVO(
            String question,
            String answer,
            String faqCategoryCode,
            boolean isActive
    ) {
        this.question = question;
        this.answer = answer;
        this.faqCategoryCode = faqCategoryCode;
        this.isActive = isActive;
    }
}
