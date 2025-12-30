package com.itwillbs.domain;

import com.itwillbs.entity.Category;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CategoryVO {

    private final Long categoryId;
    private final Long parentId;
    private final String name;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public CategoryVO(Category entity) {
        this.categoryId = entity.getCategoryId();
        this.parentId = entity.getParent() != null
                ? entity.getParent().getCategoryId()
                : null;
        this.name = entity.getName();
    }
}
