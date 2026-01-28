package com.itwillbs.mapper;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.dto.ProductSimilarDTO;
import com.itwillbs.dto.ProductSellerInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDetailMapper {

    // 조회수 증가
    void increaseViewCount(@Param("productId") Long productId);

    // 상품 상세 기본 정보
    ProductDetailDTO selectProductDetail(@Param("productId") Long productId);

    // 상세 이미지 목록
    List<String> selectProductImages(@Param("productId") Long productId);

    // 판매자 정보 (STEP 6)
    ProductSellerInfoDTO selectSellerInfo(@Param("productId") Long productId);
    
    // 유사한 상품 (같은 소분류)
    List<ProductSimilarDTO> selectSimilarBySubCategory(
            @Param("productId") Long productId,
            @Param("limit") int limit
        );
    
    	// 유사한 상품 (소분류 상품 부족하면 같은 대분류)
        List<ProductSimilarDTO> selectSimilarByParentCategory(
            @Param("productId") Long productId,
            @Param("limit") int limit,
            @Param("excludeIds") List<Long> excludeIds
        );
    }
