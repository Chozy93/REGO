package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.dto.ProductSimilarListDTO;

@Mapper
public interface ProductDetailMapper {

    // =========================
    // DETAIL01_VIEWCOUNT
    // =========================
    void increaseViewCount(@Param("productId") Long productId);

    // =========================
    // DETAIL01_INFO
    // =========================
    ProductDetailDTO selectProductDetail(
            @Param("productId") Long productId
    );

    // =========================
    // DETAIL01_SIMILAR
    // =========================
    List<ProductSimilarListDTO> selectSimilarProducts(
            @Param("productId") Long productId,
            @Param("limit") int limit
    );

    List<ProductSimilarListDTO> selectPopularProductsForSimilar(
            @Param("limit") int limit
    );
}
