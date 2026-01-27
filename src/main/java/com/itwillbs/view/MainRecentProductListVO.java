package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

public class MainRecentProductListVO {

    private final List<MainProductCardVO> items;

    public MainRecentProductListVO(List<MainProductCardVO> items) {
        this.items = items != null ? items : Collections.emptyList();
    }

    public List<MainProductCardVO> getItems() {
        return items;
    }
}
