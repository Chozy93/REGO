package com.itwillbs.view.condition;

import com.itwillbs.entity.Product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SellerProductRegisterConditionVO {

    /* =========================
       기본 정보
    ========================= */
    private String title;
    private String description;
    private Integer price;

    /* =========================
       상태 / 거래 방식 (문자열)
    ========================= */
    private String conditionStatus; // NEW, LIKE_NEW, GOOD, FAIR
    private String tradeType;        // DIRECT, DELIVERY, ALL

    /* =========================
       카테고리
    ========================= */
    private Long categoryId;

    /* =========================
       지역 (Product 테이블 그대로)
    ========================= */
    private String regionSidoCode;
    private String regionSigunguCode;
    private String regionEupmyeondongCode;
    private String regionDisplayName;
    
    
    public static SellerProductRegisterConditionVO from(Product product) {
        SellerProductRegisterConditionVO vo = new SellerProductRegisterConditionVO();

        /* =========================
           기본 정보
        ========================= */
        vo.setTitle(product.getProductName());
        vo.setDescription(product.getDescription());
        vo.setPrice(product.getPrice());

        /* =========================
           상태 / 거래 방식
           - Enum → String (폼 바인딩용)
        ========================= */
        vo.setConditionStatus(product.getConditionStatus().name());
        vo.setTradeType(product.getTradeType().name());

        /* =========================
           카테고리
        ========================= */
        vo.setCategoryId(product.getCategory().getCategoryId());

        /* =========================
           지역 정보
        ========================= */
        vo.setRegionSidoCode(product.getRegionSidoCode());
        vo.setRegionSigunguCode(product.getRegionSigunguCode());
        vo.setRegionEupmyeondongCode(product.getRegionEupmyeondongCode());
        vo.setRegionDisplayName(product.getRegionDisplayName());

        return vo;
    }

}
