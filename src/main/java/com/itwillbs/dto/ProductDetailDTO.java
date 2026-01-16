package com.itwillbs.dto;

import java.util.List;
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

    /* ===== 이미지 ===== */
    private List<String> imageUrls;

    /* ===== 판매자 ===== */
    private String sellerNickname;
    private String sellerRegion;
    private double sellerTemperature;

    /* ===== 상태 / 통계 ===== */
    private String conditionLabel;
    private int viewCount;
    private int likeCount;
    //private int chatCount;
    private boolean liked;

    /* ===== UI 표시용 ===== */
    private String createdAtDisplay;
}
