package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.dto.CategoryBaseDTO;
import com.itwillbs.dto.CategoryDTO;

@Mapper
public interface CategoryMapper {

    // ===============================
    // 기존 메서드 (절대 수정 ❌)
    // ===============================

    // 카테고리 이름 조회
    String selectCategoryNameById(Long categoryId);

    // ⭐ 부모 카테고리 기준 자식 목록 조회 (LIST00_CATEGORY_BAR)
    List<CategoryDTO> selectChildrenByParentId(Long parentId);

    // ===============================
    // ✅ 카테고리 페이지 전용 메서드 (추가)
    // ===============================

    // 1️⃣ 기준 카테고리 조회 (대/소분류 판단)
    CategoryBaseDTO selectCategoryBase(Long categoryId);

    // 2️⃣ 대분류 조회
    CategoryDTO selectParentCategory(Long categoryId);

    // 3️⃣ 소분류 목록 조회 (탭 구성용)
    List<CategoryDTO> selectSubCategories(Long parentCategoryId);
    
    CategoryBaseDTO selectDefaultCategory();

}
