package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.view.seller.ReviewFilterConditionVO;
import com.itwillbs.view.seller.SellerProductCardViewVO;
import com.itwillbs.view.seller.SellerProfileViewVO;
import com.itwillbs.view.seller.SellerReviewViewVO;

@Mapper
public interface SellerMapper {

    /* =========================
       판매자 프로필 요약
    ========================= */
    SellerProfileViewVO selectSellerProfile(
            @Param("sellerId") Long sellerId
    );

    /* =========================
       판매중 상품 (최대 15)
    ========================= */
    List<SellerProductCardViewVO> selectSellingProducts(
            @Param("sellerId") Long sellerId
    );

    /* =========================
       거래완료 상품 (최대 15)
    ========================= */
    List<SellerProductCardViewVO> selectCompletedProducts(
            @Param("sellerId") Long sellerId
    );

    /* =========================
       판매자 후기 (정렬 + offset)
    ========================= */
    List<SellerReviewViewVO> selectSellerReviews(
            @Param("sellerId") Long sellerId,
            @Param("condition") ReviewFilterConditionVO conditionVO,
            @Param("offset") int offset,
            @Param("size") int size
    );
}
