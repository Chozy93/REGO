package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

public class ProductListVO {

    private final List<MainProductCardVO> products;

    public ProductListVO(List<MainProductCardVO> products) {
        this.products = products != null ? products : Collections.emptyList();
    }

    public List<MainProductCardVO> getProducts() {
        return products;
    }
}
