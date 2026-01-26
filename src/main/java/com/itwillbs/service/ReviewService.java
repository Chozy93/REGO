package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.Review;
import com.itwillbs.entity.SellerProfile;
import com.itwillbs.entity.User;
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

        SellerProfile profile = sellerProfileRepository.findById(sellerId)
                .orElseThrow(() ->
                        new IllegalStateException("판매자 프로필이 존재하지 않습니다.")
                );

        int oldCount = profile.getTotalReviews();
        double oldAvg = profile.getRatingAvg();

        int newCount = oldCount + 1;
        double newAvg =
                ((oldAvg * oldCount) + newRating) / newCount;

        profile.increaseReviews();   // total_reviews +1
        profile.applyRating(newAvg); // rating_avg 반영

        // 영속 상태라 save 생략 가능하지만 명시적으로
        sellerProfileRepository.save(profile);
    }

}
