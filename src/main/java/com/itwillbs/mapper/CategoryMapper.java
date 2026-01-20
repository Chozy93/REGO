package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.dto.CategoryDTO;

@Mapper
public interface CategoryMapper {

    // 카테고리 이름 조회
    String selectCategoryNameById(Long categoryId);

    // ⭐ 부모 카테고리 기준 자식 목록 조회 (LIST00_CATEGORY_BAR)
    List<CategoryDTO> selectChildrenByParentId(Long parentId);
}
