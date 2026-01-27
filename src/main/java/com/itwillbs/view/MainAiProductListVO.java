package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

public class MainAiProductListVO {

    private final List<MainProductCardVO> items;

    public MainAiProductListVO(List<MainProductCardVO> items) {
        this.items = items != null ? items : Collections.emptyList();
    }

    public List<MainProductCardVO> getItems() {
        return items;
    }
}
