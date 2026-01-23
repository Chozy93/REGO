package com.itwillbs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryBaseDTO {

    private final Long categoryId;
    private final String name;
    private final Long parentId;
    private final int level;
}
