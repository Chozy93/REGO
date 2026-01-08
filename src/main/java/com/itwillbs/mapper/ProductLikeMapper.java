package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductLikeMapper {
	
	// 1️⃣ 찜 여부 확인
	boolean exists(@Param("userId") Long userId,
				   @Param("productId") Long productId);
	
	// 2️⃣ 찜 추가
	void insert(@Param("userId") Long userId,
			    @Param("productId") Long productId);
	
	// 3️⃣ 찜 삭제
	void delete(@Param("userId") Long userId,
                @Param("productId") Long productId);
	
	// 4️⃣ 상품 찜 개수
	int countByProductId(@Param("productId") Long productId);
	
}
