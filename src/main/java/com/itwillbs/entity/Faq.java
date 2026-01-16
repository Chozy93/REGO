package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.FaqVO;

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
       카테고리 (코드)
    ========================= */
    @Column(name = "faq_category_id", length = 50)
    private String faqCategoryId;

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
       생성자 (VO → Entity)
    ========================= */
    public Faq(FaqVO vo) {
        this.question = vo.getQuestion();
        this.answer = vo.getAnswer();
        this.faqCategoryId = vo.getFaqCategoryId();
        this.isActive = vo.isActive();
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
    public void update(FaqVO vo) {
        this.question = vo.getQuestion();
        this.answer = vo.getAnswer();
        this.faqCategoryId = vo.getFaqCategoryId();
        this.isActive = vo.isActive();
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
