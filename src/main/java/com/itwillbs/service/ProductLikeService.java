package com.itwillbs.service;

import com.itwillbs.mapper.ProductLikeMapper;
import com.itwillbs.view.ProductLikeResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    // 🔥 로그인 전 임시 테스트 유저
    private static final Long TEST_USER_ID = 1L;

    private final ProductLikeMapper productLikeMapper;
    
    // ✅ 1️⃣ 찜 여부 조회 (상세/메인 공용)
    public boolean isLiked(Long productId, Long userId) {
        return productLikeMapper.exists(userId, productId);
    }

    // ✅ 2️⃣ 찜 개수 조회 (상세/메인 공용)
    public int getLikeCount(Long productId) {
        return productLikeMapper.countByProductId(productId);
    }
    
    // ✅ 3️⃣ 찜 토글 
    @Transactional
    public ProductLikeResultVO toggleLike(Long productId, Long userId) {

        // 1️⃣ 현재 찜 상태 확인
        boolean liked = productLikeMapper.exists(userId, productId);

        
        if (liked) {
            productLikeMapper.delete(userId, productId);
        } else {
            productLikeMapper.insert(userId, productId);
        }
        
        // 3결과 상태는 반전값
        boolean nowLiked = !liked;

        // 찜 개수 재조회
        int likeCount = productLikeMapper.countByProductId(productId);
        
     
        return new ProductLikeResultVO(
                productId.toString(),
                likeCount,
                nowLiked
        );
    }
}
