package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.FaqCategory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FaqCategoryVO {

    private final Long categoryId;
    private final String categoryName;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO (조회/출력)
    ========================= */
    public FaqCategoryVO(FaqCategory entity) {
        this.categoryId = entity.getCategoryId();
        this.categoryName = entity.getCategoryName();
        this.isActive = entity.isActive();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
       등록/수정용
    ========================= */
    public FaqCategoryVO(String categoryName, boolean isActive) {
        this.categoryId = null;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
