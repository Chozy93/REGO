package com.itwillbs.dto;

import java.util.List;

import com.itwillbs.entity.enumtype.ProductSalesStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailDTO {

    /* ===== 상품 기본 ===== */
    private Long productId;
    private String productName;
    private int price;
    private String priceDisplay;
    private String description;

    /* ✅ 대표 이미지 (추가) */
    private String mainImageUrl;

    /* ===== 상세 이미지 ===== */
    private List<String> imageUrls;

    /* ===== 판매자 ===== */
    private String sellerNickname;
    private String sellerRegion;
    private double sellerTemperature;

    /* ===== 상태 / 통계 ===== */
    private String conditionLabel;
    private int viewCount;
    private int likeCount;
    private boolean liked;
    
    // ✅ 카테고리용 (추가)
    private Long categoryId;          // 소분류 ID
    private String categoryName;      // 소분류 이름

    private Long parentCategoryId;    // 대분류 ID
    private String parentCategoryName;// 대분류 이름
    
    /* ====== 판매 상태 ====== */
    private ProductSalesStatus salesStatus;   // DTO에서는 enum 허용

    /* ===== UI 표시용 ===== */
    private String createdAtDisplay;
}
