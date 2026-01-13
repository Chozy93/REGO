package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.dto.ProductDetailDTO;

@Mapper
public interface ProductDetailMapper {
	
	void increaseViewCount(Long productId);

    ProductDetailDTO selectProductDetail(Long productId);
}


