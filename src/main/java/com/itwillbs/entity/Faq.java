package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.FaqVO;
import com.itwillbs.entity.enumtype.FaqCategory;
import com.itwillbs.view.condition.FaqCreateConditionVO;

@Entity
@Table(name = "faqs")
@Getter
public class Faq {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faqs_id")
    private Long faqsId;

    /* =========================
       질문 / 답변
    ========================= */
    @Column(name = "question", length = 255, nullable = false)
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    /* =========================
       카테고리 (ENUM)
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "faq_category", nullable = false, length = 30)
    private FaqCategory faqCategory;

    /* =========================
       활성 상태
    ========================= */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected Faq() {}

    /* =========================
       생성자 (명시적, 정석)
    ========================= */
    public Faq(
    		FaqCreateConditionVO faqCreateConditionVO
    ) {
        this.question = faqCreateConditionVO.getQuestion();
        this.answer = faqCreateConditionVO.getAnswer();
        this.faqCategory = FaqCategory.fromCode(faqCreateConditionVO.getFaqCategoryCode());
        this.isActive = faqCreateConditionVO.isActive();
        this.createdAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public FaqVO toVO() {
        return new FaqVO(this);
    }

    /* =========================
       상태 / 내용 변경
    ========================= */
    public void update(
            String question,
            String answer,
            FaqCategory faqCategory,
            boolean isActive
    ) {
        this.question = question;
        this.answer = answer;
        this.faqCategory = faqCategory;
        this.isActive = isActive;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
}
