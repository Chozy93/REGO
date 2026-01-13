package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    String selectCategoryNameById(Long categoryId);
}
