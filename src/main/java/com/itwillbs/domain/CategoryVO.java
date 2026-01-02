package com.itwillbs.domain;

import com.itwillbs.entity.Category;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CategoryVO {

    private final Long categoryId;
    private final Long parentId;

    private final Integer level;
    private final String name;
    private final Integer sortOrder;
    private final Boolean isActive;

    /* =========================
       Entity → VO
    ========================= */
    public CategoryVO(Category entity) {
        this.categoryId = entity.getCategoryId();
        this.parentId = entity.getParent() != null
                ? entity.getParent().getCategoryId()
                : null;

        this.level = entity.getLevel();
        this.name = entity.getName();
        this.sortOrder = entity.getSortOrder();
        this.isActive = entity.getIsActive();
    }
}
