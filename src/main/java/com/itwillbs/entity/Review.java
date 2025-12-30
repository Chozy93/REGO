package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.ReviewVO;

@Entity
@Table(name = "reviews")
@Getter
public class Review {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long reviewId;

    /* =========================
       상품
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_reviews_product")
    )
    private Product product;

    /* =========================
       구매자 (작성자)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "buyer_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_reviews_buyer")
    )
    private User buyer;

    /* =========================
       판매자 ID (대상)
    ========================= */
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    /* =========================
       리뷰 내용
    ========================= */
    @Column(name = "content", nullable = false)
    private String content;

    /* =========================
       별점
    ========================= */
    @Column(name = "rating", nullable = false)
    private int rating;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected Review() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public Review(Product product, User buyer, Long sellerId, ReviewVO vo) {
        this.product = product;
        this.buyer = buyer;
        this.sellerId = sellerId;
        this.content = vo.getContent();
        this.rating = vo.getRating();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public ReviewVO toVO() {
        return new ReviewVO(this);
    }

    /* =========================
       수정
    ========================= */
    public void update(ReviewVO vo) {
        this.content = vo.getContent();
        this.rating = vo.getRating();
        this.updatedAt = LocalDateTime.now();
    }
}
