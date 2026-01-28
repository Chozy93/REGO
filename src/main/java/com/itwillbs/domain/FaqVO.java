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

    /* =========================
       카테고리 (VO는 enum 모름)
    ========================= */
    private final String faqCategoryCode;
    private final String faqCategoryLabel;

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

        this.faqCategoryCode = entity.getFaqCategory().name();
        this.faqCategoryLabel = entity.getFaqCategory().getLabel();

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
            String faqCategoryCode,
            boolean isActive
    ) {
        this.faqsId = null;
        this.question = question;
        this.answer = answer;

        this.faqCategoryCode = faqCategoryCode;
        this.faqCategoryLabel = null; // 서버에서 채움

        this.isActive = isActive;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
