package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.ProductVO;
import com.itwillbs.entity.enumtype.ProductConditionStatus;
import com.itwillbs.entity.enumtype.ProductSalesStatus;
import com.itwillbs.entity.enumtype.TradeType;

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
    @Column(name = "region_display_name", length = 100, nullable = false)
    private String regionDisplayName;

    @Column(name = "region_code", length = 20, nullable = false)
    private String regionCode;

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
    @Column(name = "main_image_url", length = 500, nullable = false)
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

     this.conditionStatus = ProductConditionStatus.valueOf(vo.getConditionStatusCode());
     this.salesStatus = ProductSalesStatus.ON_SALE;

     this.regionDisplayName = vo.getRegionDisplayName();
     this.regionCode = vo.getRegionCode();

     this.tradeType = TradeType.valueOf(vo.getTradeTypeCode());
     this.mainImageUrl = vo.getMainImageUrl();

     this.viewCount = 0;
     this.likeCount = 0;
     this.createdAt = LocalDateTime.now();
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

    public void changeSalesStatus(ProductSalesStatus status) {
        this.salesStatus = status;
    }
}
