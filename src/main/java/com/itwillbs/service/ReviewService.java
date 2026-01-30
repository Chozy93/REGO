package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.dto.ReviewDTO;
import com.itwillbs.entity.Review;
import com.itwillbs.entity.SellerProfile;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.MypageMapper;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.repository.ReviewRepository;
import com.itwillbs.repository.SellerProfileRepository;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.view.condition.ReviewConditionVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final MypageMapper mypageMapper;

    public void writeReview(ReviewConditionVO conditionVO) {

        /* =========================
           1. 로그인 유저 (구매자)
        ========================= */
        User buyer = SecurityUtil.getCurrentUser();
        if (buyer == null) {
            throw new IllegalStateException("로그인 유저가 없습니다.");
        }

        /* =========================
        2. 상품 / 판매자 존재 검증
     ========================= */
     if (!productRepository.existsById(conditionVO.getProductId())) {
         throw new IllegalArgumentException("존재하지 않는 상품입니다.");
     }

     if (!userRepository.existsById(conditionVO.getSellerId())) {
         throw new IllegalArgumentException("존재하지 않는 판매자입니다.");
     }

        /* =========================
           3. Review 엔티티 생성
        ========================= */
        Review review = new Review(buyer,conditionVO);
                
        /* =========================
           4. 저장
        ========================= */
        reviewRepository.save(review);
        

        /* =========================
           5. seller_profile 평점 갱신
        ========================= */
        updateSellerProfileRating(
                conditionVO.getSellerId(),
                conditionVO.getRating()
        );
    }
    
    
    private void updateSellerProfileRating(Long sellerId, int newRating) {
    	  // 방어: 평점 범위
        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이여야 합니다.");
        }

        SellerProfile profile = sellerProfileRepository.findById(sellerId)
                .orElseThrow(() ->
                        new IllegalStateException("판매자 프로필이 존재하지 않습니다.")
                );

        int oldCount = profile.getTotalReviews();
        double oldAvg = profile.getRatingAvg();

        int newCount = oldCount + 1;
        double rawAvg =
                ((oldAvg * oldCount) + newRating) / newCount;

        // ⭐ 소수점 1자리 올림 정책
        double newAvg = Math.ceil(rawAvg * 10) / 10.0;

        profile.increaseReviews();   // total_reviews +1
        profile.applyRating(newAvg); // rating_avg 반영

        // 영속 상태라 save 생략 가능하지만 명시적으로
        sellerProfileRepository.save(profile);
    }

    public boolean checkIfReviewed(Long buyerId, Long productId) {
        return reviewRepository.existsByBuyerUserIdAndProductId(buyerId, productId);
    }
    
    public ReviewDTO getReviewByProductAndBuyer(Long productId, Long buyerId) {
        // 1. DB에서 리뷰 찾기
        Review review = reviewRepository.findByBuyerUserIdAndProductId(buyerId, productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        User seller = userRepository.findById(review.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("DB에 저장된 sellerId(" + review.getSellerId() + ")가 유저 테이블에 없습니다!"));
        
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보를 찾을 수 없습니다."));
        
        // 2. DTO로 변환해서 반환
        ReviewDTO dto = new ReviewDTO();
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        dto.setSellerNickname(seller.getNickname());
        dto.setProductImageUrl(product.getMainImageUrl());
        return dto;
    }


	public ReviewDTO getReviewByProductId(Long productId) {

    ReviewDTO review = mypageMapper.getReviewDetailByProductId(productId);
    
    if (review == null) {
        throw new RuntimeException("해당 상품의 리뷰를 찾을 수 없습니다.");
    }
    
    return review;
}
    
}
