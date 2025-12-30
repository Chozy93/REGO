package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.Faq;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FaqVO {

    private final Long faqsId;
    private final String question;
    private final String answer;
    private final String faqCategoryId;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO (조회/출력)
    ========================= */
    public FaqVO(Faq entity) {
        this.faqsId = entity.getFaqsId();
        this.question = entity.getQuestion();
        this.answer = entity.getAnswer();
        this.faqCategoryId = entity.getFaqCategoryId();
        this.isActive = entity.isActive();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
       등록/수정용 생성자
    ========================= */
    public FaqVO(
            String question,
            String answer,
            String faqCategoryId,
            boolean isActive
    ) {
        this.faqsId = null;
        this.question = question;
        this.answer = answer;
        this.faqCategoryId = faqCategoryId;
        this.isActive = isActive;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
