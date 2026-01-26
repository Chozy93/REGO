package com.itwillbs.view.condition;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductListConditionVO {

    /**
     * 카테고리 ID
     * - 소분류 클릭 시: 해당 카테고리 ID
     * - 대분류 클릭 시: 부모 카테고리 ID
     */
    private Long categoryId;

    /**
     * 대분류 여부
     * true  : 대분류 클릭 (하위 카테고리 전부 조회)
     * false : 소분류 클릭 (해당 카테고리만 조회)
     */
    private boolean parent;
}
