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

    // =========================
    // 비슷한 상품
    // =========================
    List<ProductSimilarDTO> selectSimilarProducts(
        @Param("productId") Long productId,
        @Param("limit") int limit
    );

    // =========================
    // 비슷한 상품 fallback (인기 상품)
    // =========================
    List<ProductSimilarDTO> selectPopularProductsForSimilar(
        @Param("limit") int limit
    );
}
