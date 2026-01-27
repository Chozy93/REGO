package com.itwillbs.ai;

import com.itwillbs.view.MainProductCardVO;
import java.util.Collections;
import java.util.List;

public class AIRecommendProductListVO {

    private final List<MainProductCardVO> items;

    public AIRecommendProductListVO(List<MainProductCardVO> items) {
        this.items = items != null ? items : Collections.emptyList();
    }

    public List<MainProductCardVO> getItems() {
        return items;
    }
}
