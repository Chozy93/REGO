package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.FaqCategoryVO;

@Entity
@Table(name = "faq_categories")
@Getter
public class FaqCategory {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    /* =========================
       카테고리명
    ========================= */
    @Column(name = "category_name", length = 50, nullable = false)
    private String categoryName;

    /* =========================
       노출 여부
    ========================= */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected FaqCategory() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public FaqCategory(FaqCategoryVO vo) {
        this.categoryName = vo.getCategoryName();
        this.isActive = vo.isActive();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public FaqCategoryVO toVO() {
        return new FaqCategoryVO(this);
    }

    /* =========================
       상태 변경
    ========================= */
    public void update(FaqCategoryVO vo) {
        this.categoryName = vo.getCategoryName();
        this.isActive = vo.isActive();
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
}
