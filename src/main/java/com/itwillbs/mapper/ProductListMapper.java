package com.itwillbs.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.MainProductListDTO;
import com.itwillbs.dto.ProductListByCategoryDTO;
import com.itwillbs.view.MainProductCardVO;

@Mapper
public interface ProductListMapper {

    List<ProductListByCategoryDTO> selectProductsByCategoryWithChildren(
        @Param("categoryId") Long categoryId,
        @Param("loginUserId") Long loginUserId
    );

    List<MainProductCardVO> selectProductsByParent(Long parentId);



}

