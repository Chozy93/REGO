package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.dto.SellerInfoDTO;

@Mapper
public interface SellerInfoMapper {
	
	SellerInfoDTO selectSellerInfoByProductId(Long productId);

}
