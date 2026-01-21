package com.itwillbs.domain;

import java.time.format.DateTimeFormatter;

import com.itwillbs.entity.SellerProfile;
import com.itwillbs.entity.enumtype.SellerStatus;
import com.itwillbs.view.condition.SellerRegisterConditionVO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerProfileVO {

    private final Long sellerId;

    private final String description;

    private final Double ratingAvg;
    private final Integer ratingCount;
    private final Integer totalSales;
    private final Integer totalReviews;

    /* =========================
       판매자 상태 (enum → String)
    ========================= */
    private final String sellerStatusCode;   // ACTIVE / SUSPENDED
    private final String sellerStatusLabel;  // 판매 가능 / 판매 중지

    /* =========================
       날짜 (String)
    ========================= */
    private final String termsAgreedAt;
    private final String createdAt;
    private final String updatedAt;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    
    /* =========================
    생성 요청용 생성자
    - Entity 생성 재료
    - 초기 상태 결정 ❌
 ========================= */
 public SellerProfileVO(SellerRegisterConditionVO conditionVO) {
     this.sellerId = null;
     this.description = conditionVO.getDescription();

     this.ratingAvg = null;
     this.ratingCount = null;
     this.totalSales = null;
     this.totalReviews = null;

     this.sellerStatusCode = null;
     this.sellerStatusLabel = null;

     this.termsAgreedAt = null;
     this.createdAt = null;
     this.updatedAt = null;
 }

    /* =========================
       Entity → VO 생성자
    ========================= */
    public SellerProfileVO(SellerProfile entity) {

        this.sellerId = entity.getSellerId();
        this.description = entity.getDescription();

        this.ratingAvg = entity.getRatingAvg();
        this.ratingCount = entity.getRatingCount();
        this.totalSales = entity.getTotalSales();
        this.totalReviews = entity.getTotalReviews();

        SellerStatus status = entity.getSellerStatus();
        this.sellerStatusCode = status.name();
        this.sellerStatusLabel = status.getLabel();

        this.termsAgreedAt = entity.getTermsAgreedAt()
                .format(DATE_TIME_FORMATTER);

        this.createdAt = entity.getCreatedAt()
                .format(DATE_TIME_FORMATTER);

        this.updatedAt = entity.getUpdatedAt()
                .format(DATE_TIME_FORMATTER);
    }
}
