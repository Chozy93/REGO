package com.itwillbs.view;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.entity.enumtype.ProductSalesStatus;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ProductDetailVO {

    /* ===== 상품 기본 ===== */
    private final String productId;
    private final String productName;
    private final int price;
    private final String priceDisplay;
    private final String description;
    private String mainImageUrl;
    
    /* ===== 이미지 ===== */
    private final List<String> imageUrls;

    /* ===== 판매자 ===== */
    private final String sellerNickname;
    private final String sellerRegion;
    private final String sellerTemperature;

    /* ===== 상태 / 통계 ===== */
    private final String conditionLabel;
    private final int viewCount;
    private int likeCount;
    private final int chatCount;
    private boolean liked;
    
    /* ===== 카테고리 ===== */
    private final String categoryId;           // 소분류 ID
    private final String categoryName;         // 소분류 이름
    private final String parentCategoryId;     // 대분류 ID
    private final String parentCategoryName;   // 대분류 이름

    
    /* ===== 판매 상태 ===== */
    private final String salesStatusCode;   // ON_SALE / RESERVED / SOLD
    private final String salesStatusLabel;  // 판매중 / 예약중 / 판매완료


    /* ===== UI 표시용 ===== */
    private final String createdAtDisplay;

    /* ===== 판매자 카드 ===== */
    private ProductSellerInfoVO seller;

    /* =========================
       ✅ DTO 기반 생성자 (핵심)
    ========================= */
    public ProductDetailVO(ProductDetailDTO dto) {

        this.productId = String.valueOf(dto.getProductId());
        this.productName = dto.getProductName();
        this.price = dto.getPrice();
        this.priceDisplay = dto.getPriceDisplay();
        this.mainImageUrl = dto.getMainImageUrl();

        /* DETAIL01_INFO */
        this.description =
            dto.getDescription() != null && !dto.getDescription().isBlank()
                ? dto.getDescription()
                : """
                  판매자가 상품 설명을 아직 작성하지 않았습니다.

                  · 상품 상태는 사진을 참고해주세요.
                  · 직거래 / 택배 거래 모두 가능합니다.
                  · 추가 문의는 채팅으로 부탁드립니다.
                  """;

        /* 이미지 */
        this.imageUrls =
            dto.getImageUrls() != null
                ? dto.getImageUrls()
                : Collections.emptyList();

        /* 판매자 */
        this.sellerNickname =
            dto.getSellerNickname() != null ? dto.getSellerNickname() : "알 수 없음";
        this.sellerRegion =
            dto.getSellerRegion() != null ? dto.getSellerRegion() : "";
        this.sellerTemperature =
            String.valueOf(dto.getSellerTemperature());

        /* 상태 / 통계 */
        this.conditionLabel = dto.getConditionLabel();
        this.viewCount = dto.getViewCount();
        this.likeCount = dto.getLikeCount();
        this.chatCount = 0; // 데이터 없으면 기본값
        this.liked = dto.isLiked();
        
        /* ===== 판매 상태 (DETAIL01_STATUS 핵심) ===== */
        ProductSalesStatus status = dto.getSalesStatus();
        this.salesStatusCode = status.name();
        this.salesStatusLabel = status.getLabel();

        this.createdAtDisplay = dto.getCreatedAtDisplay();
        
        /* ===== 카테고리 ===== */
        this.categoryId =
                dto.getCategoryId() != null
                        ? String.valueOf(dto.getCategoryId())
                        : null;

        this.categoryName = dto.getCategoryName();

        this.parentCategoryId =
                dto.getParentCategoryId() != null
                        ? String.valueOf(dto.getParentCategoryId())
                        : null;

        this.parentCategoryName = dto.getParentCategoryName();

    }

    /* ===== getter / setter는 기존 그대로 유지 ===== */
}
