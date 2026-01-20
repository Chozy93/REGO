package com.itwillbs.view.product.list;

import java.util.List;
import lombok.Getter;

@Getter
public class CategoryBarVO {

    private final String categoryName;   // ⭐ 반드시 이 이름
    private final boolean parentCategory;
    private final List<CategoryTabVO> tabs;

    public CategoryBarVO(
            String categoryName,
            boolean parentCategory,
            List<CategoryTabVO> tabs
    ) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
        this.tabs = tabs;
    }
}
