package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itwillbs.entity.Review;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 1. 리뷰가 존재하는지 확인할 때
    boolean existsByBuyerUserIdAndProductId(Long userId, Long productId);

    // 2. 리뷰 상세 내용을 가져올 때
    Optional<Review> findByBuyerUserIdAndProductId(Long userId, Long productId);
    
}