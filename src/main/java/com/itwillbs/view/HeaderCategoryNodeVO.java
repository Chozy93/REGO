package com.itwillbs.view;

import java.util.List;

import com.itwillbs.domain.CategoryVO;

import lombok.Getter;
import lombok.ToString;

// view
@Getter
@ToString
public class HeaderCategoryNodeVO {

    private final CategoryVO parent;
    private final String iconCode;     // ← 조회 필드 추가
    private final List<CategoryVO> children;

    public HeaderCategoryNodeVO(
            CategoryVO parent,
            String iconCode,
            List<CategoryVO> children
    ) {
        this.parent = parent;
        this.iconCode = iconCode;
        this.children = children;
    }
}
