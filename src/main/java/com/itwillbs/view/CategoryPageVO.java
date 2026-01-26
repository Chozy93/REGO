package com.itwillbs.view;

public class CategoryPageVO {

    private final CategoryGroupVO categoryGroup;
    private final ProductListVO productList;

    public CategoryPageVO(
            CategoryGroupVO categoryGroup,
            ProductListVO productList
    ) {
        this.categoryGroup = categoryGroup;
        this.productList = productList;
    }

    public CategoryGroupVO getCategoryGroup() {
        return categoryGroup;
    }

    public ProductListVO getProductList() {
        return productList;
    }
}
