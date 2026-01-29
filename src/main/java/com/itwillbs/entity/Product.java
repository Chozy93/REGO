package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.ProductVO;
import com.itwillbs.entity.enumtype.ProductConditionStatus;
import com.itwillbs.entity.enumtype.ProductSalesStatus;
import com.itwillbs.entity.enumtype.TradeType;
import com.itwillbs.view.condition.SellerProductRegisterConditionVO;

@Entity
@Table(name = "products")
@Getter
public class Product {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    /* =========================
       판매자
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "seller_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_products_seller")
    )
    private User seller;

    /* =========================
    구매자 (추가!)
========================= */
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "buyer_id")
private User buyer;
    
    
    /* =========================
       카테고리
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "category_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_products_category")
    )
    private Category category;

    /* =========================
       상품 정보
    ========================= */
    @Column(name = "product_name", length = 200, nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_status", nullable = false)
    private ProductConditionStatus conditionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sales_status", nullable = false)
    private ProductSalesStatus salesStatus;

    /* =========================
	    지역 정보
	 ========================= */
	 @Column(name = "region_sido_code", length = 20)
	 private String regionSidoCode;
	
	 @Column(name = "region_sigungu_code", length = 20)
	 private String regionSigunguCode;
	
	 @Column(name = "region_eupmyeondong_code", length = 20)
	 private String regionEupmyeondongCode;
	
	 @Column(name = "region_display_name", length = 100)
	 private String regionDisplayName;


    /* =========================
       통계
    ========================= */
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    /* =========================
       거래 방식
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    private TradeType tradeType;

    /* =========================
       이미지
    ========================= */
    @Column(name = "main_image_url", length = 500, nullable = true)
    private String mainImageUrl;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected Product() {}

    /* =========================
    생성자 (VO → Entity)
 ========================= */
    public Product(User seller, Category category, ProductVO vo) {
        this.seller = seller;
        this.category = category;

        this.productName = vo.getProductName();
        this.description = vo.getDescription();
        this.price = vo.getPrice();

        this.conditionStatus =
            ProductConditionStatus.valueOf(vo.getConditionStatusCode());
        this.salesStatus = ProductSalesStatus.ON_SALE;

        /* 지역 */
        this.regionSidoCode = vo.getRegionSidoCode();
        this.regionSigunguCode = vo.getRegionSigunguCode();
        this.regionEupmyeondongCode = vo.getRegionEupmyeondongCode();
        this.regionDisplayName = vo.getRegionDisplayName();

        this.tradeType = TradeType.valueOf(vo.getTradeTypeCode());
        this.mainImageUrl = vo.getMainImageUrl();

        this.viewCount = 0;
        this.likeCount = 0;
        this.createdAt = LocalDateTime.now();
    }

    public static Product createByRegisterCondition(
            User seller,
            Category category,
            SellerProductRegisterConditionVO conditionVO,
            String mainImageUrl
    ) {
        Product product = new Product();

        /* =========================
           연관 엔티티
        ========================= */
        product.seller = seller;
        product.category = category;

        /* =========================
           상품 기본 정보
        ========================= */
        product.productName = conditionVO.getTitle();
        product.description = conditionVO.getDescription();
        product.price = conditionVO.getPrice();

        /* =========================
           상품 상태 (문자열 → Enum)
           - label/code 비교는 enum 내부에서
        ========================= */
        product.conditionStatus =
            ProductConditionStatus.from(conditionVO.getConditionStatus());

        product.tradeType =
            TradeType.from(conditionVO.getTradeType());

        /* =========================
           판매 상태 (초기값)
        ========================= */
        product.salesStatus = ProductSalesStatus.ON_SALE;

        /* =========================
           지역 정보
        ========================= */
        product.regionSidoCode = conditionVO.getRegionSidoCode();
        product.regionSigunguCode = conditionVO.getRegionSigunguCode();
        product.regionEupmyeondongCode = conditionVO.getRegionEupmyeondongCode();
        product.regionDisplayName = conditionVO.getRegionDisplayName();

        /* =========================
           이미지 (대표 이미지)
           - nullable=false 이므로 반드시 필요
        ========================= */
        product.mainImageUrl = mainImageUrl;

        /* =========================
           통계 초기값
        ========================= */
        product.viewCount = 0;
        product.likeCount = 0;

        /* =========================
           생성 시각
        ========================= */
        product.createdAt = LocalDateTime.now();

        return product;
    }

    /* =========================
       Entity → VO
    ========================= */
    public ProductVO toVO() {
        return new ProductVO(this);
    }

    /* =========================
       상태 변경
    ========================= */
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) this.likeCount--;
    }

    public void changeSalesStatus(ProductSalesStatus status, User buyer) {
        this.salesStatus = status;
        this.buyer = buyer;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateFromCondition(
    	    SellerProductRegisterConditionVO conditionVO,
    	    Category category,
    	    String mainImageUrl
    	) {
    	    /* =========================
    	       상품 기본 정보
    	    ========================= */
    	    this.productName = conditionVO.getTitle();
    	    this.description = conditionVO.getDescription();
    	    this.price = conditionVO.getPrice();
    	    this.mainImageUrl=mainImageUrl;
    	    /* =========================
    	       상품 상태 / 거래 방식
    	    ========================= */
    	    this.conditionStatus =
    	        ProductConditionStatus.from(conditionVO.getConditionStatus());

    	    this.tradeType =
    	        TradeType.from(conditionVO.getTradeType());

    	    /* =========================
    	       카테고리
    	    ========================= */
    	    this.category = category;

    	    /* =========================
    	       지역 정보
    	    ========================= */
    	    this.regionSidoCode = conditionVO.getRegionSidoCode();
    	    this.regionSigunguCode = conditionVO.getRegionSigunguCode();
    	    this.regionEupmyeondongCode = conditionVO.getRegionEupmyeondongCode();
    	    this.regionDisplayName = conditionVO.getRegionDisplayName();

    	    /* =========================
    	       수정 시각
    	    ========================= */
    	    this.updatedAt = LocalDateTime.now();
    	}


}
