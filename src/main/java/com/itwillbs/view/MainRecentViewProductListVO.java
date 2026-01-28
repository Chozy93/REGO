package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

public class MainRecentViewProductListVO {

    private final List<MainProductCardVO> items;

    public MainRecentViewProductListVO(List<MainProductCardVO> items) {
        this.items = items != null ? items : Collections.emptyList();
    }

    public List<MainProductCardVO> getItems() {
        return items;
    }
}
