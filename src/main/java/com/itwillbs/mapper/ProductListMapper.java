package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.ProductListByCategoryDTO;

@Mapper
public interface ProductListMapper {
	
	List<ProductListByCategoryDTO> selectProductsByCategoryWithChildren(
			@Param("categoryId") Long categoryId
			);
	
	
	

}
