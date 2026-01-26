package com.itwillbs.view.seller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewFilterConditionVO {

    private final ReviewSortType sortType;

    public ReviewFilterConditionVO(ReviewSortType sortType) {
        this.sortType = sortType != null
                ? sortType
                : ReviewSortType.LATEST; // 기본값
    }
}
