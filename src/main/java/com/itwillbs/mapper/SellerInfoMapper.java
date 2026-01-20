package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.dto.ProductSellerInfoDTO;

@Mapper
public interface SellerInfoMapper {
	
	ProductSellerInfoDTO selectSellerInfoByProductId(Long productId);

}
