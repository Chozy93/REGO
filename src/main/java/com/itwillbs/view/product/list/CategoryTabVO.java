package com.itwillbs.view.product.list;

import lombok.Getter;

@Getter
public class CategoryTabVO {

    /** 카테고리 ID (URL 용) */
    private final String categoryId;

    /** 표시 이름 */
    private final String name;

    /** 현재 선택된 탭 여부 */
    private final boolean active;

    public CategoryTabVO(
            String categoryId,
            String name,
            boolean active
    ) {
        this.categoryId = categoryId;
        this.name = name;
        this.active = active;
    }
}
