package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.ProductCategoryItemDTO;

public interface ProductCategoryMapper {

    List<ProductCategoryItemDTO> selectProductsByCategory(
            @Param("categoryId") Long categoryId,
            @Param("loginUserId") Long loginUserId
    );
}
