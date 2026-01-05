package com.itwillbs.view;


import java.util.List;

import com.itwillbs.domain.CategoryVO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HeaderCategoryListVO {

    private final List<HeaderCategoryNodeVO> categories;

    public HeaderCategoryListVO(List<HeaderCategoryNodeVO> categories) {
        this.categories = categories;
    }
}

