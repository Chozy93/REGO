package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public class CategoryGroupVO {

    private final String parentCategoryId;
    private final String parentCategoryName;
    private final List<SubCategoryVO> subCategories;
    private final String selectedCategoryId;

    public CategoryGroupVO(
            String parentCategoryId,
            String parentCategoryName,
            List<SubCategoryVO> subCategories,
            String selectedCategoryId
    ) {
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
        this.subCategories =
                subCategories != null ? subCategories : Collections.emptyList();
        this.selectedCategoryId = selectedCategoryId;
    }
}
