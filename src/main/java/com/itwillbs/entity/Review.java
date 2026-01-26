package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.ReviewVO;
import com.itwillbs.view.condition.ReviewConditionVO;

@Entity
@Table(
	    name = "reviews",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            name = "uk_reviews_buyer_product",
	            columnNames = {"buyer_id", "product_id"}
	        )
	    }
	)
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
    @Column(name = "product_id", nullable = false)
    private Long productId;

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
    public Review(Long productId, User buyer, Long sellerId, ReviewVO vo) {
        validateRating(vo.getRating());

        this.productId = productId;
        this.buyer = buyer;
        this.sellerId = sellerId;
        this.content = vo.getContent();
        this.rating = vo.getRating();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    /* =========================
    최초 생성자 (도메인 규칙)
    - buyer + conditionVO
 ========================= */
 public Review(
         User buyer,
         ReviewConditionVO conditionVO
 ) {
     this.productId = conditionVO.getProductId();
     this.buyer = buyer;

     this.sellerId = conditionVO.getSellerId();
     this.rating   = conditionVO.getRating();
     this.content  = conditionVO.getContent();

     this.createdAt = LocalDateTime.now();
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
        validateRating(vo.getRating());

        this.content = vo.getContent();
        this.rating = vo.getRating();
        this.updatedAt = LocalDateTime.now();
    }
    
    
    private static void validateRating(int rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("별점은 1~10점 사이여야 합니다.");
        }
    }
}
