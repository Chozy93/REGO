package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import com.itwillbs.domain.ProductImageVO;

@Entity
@Table(name = "product_images")
@Getter
public class ProductImage {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    /* =========================
       상품 (N:1)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_product_images_product")
    )
    private Product product;

    /* =========================
       이미지 경로
    ========================= */
    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;

    /* =========================
       정렬 순서
    ========================= */
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected ProductImage() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public ProductImage(Product product, ProductImageVO vo) {
        this.product = product;
        this.imageUrl = vo.getImageUrl();
        this.sortOrder = vo.getSortOrder();
    }
    //이미지 생성 메서드
    public static ProductImage create(
            Product product,
            String imageUrl,
            int sortOrder
    ) {
        ProductImage image = new ProductImage();
        image.product = product;
        image.imageUrl = imageUrl;
        image.sortOrder = sortOrder;
        return image;
    }
    /* =========================
       Entity → VO
    ========================= */
    public ProductImageVO toVO() {
        return new ProductImageVO(this);
    }
}
