package com.itwillbs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryDTO {

    private final Long categoryId;
    private final String name;
}
