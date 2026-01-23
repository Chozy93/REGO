package com.itwillbs.view;

public class SubCategoryVO {

    private final String categoryId;
    private final String categoryName;
    private final boolean isSelected;

    public SubCategoryVO(
            String categoryId,
            String categoryName,
            boolean isSelected
    ) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isSelected = isSelected;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
