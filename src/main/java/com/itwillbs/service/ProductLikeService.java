package com.itwillbs.service;

import com.itwillbs.entity.User;
import com.itwillbs.mapper.ProductLikeMapper;
import com.itwillbs.view.ProductLikeResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    private final ProductLikeMapper productLikeMapper;

    /* =========================
       찜 여부 조회
    ========================= */
    public boolean isLiked(Long productId, Long userId) {
        return productLikeMapper.exists(userId, productId);
    }

    /* =========================
       찜 개수 조회
    ========================= */
    public int getLikeCount(Long productId) {
        return productLikeMapper.countByProductId(productId);
    }

    /* =========================
       ❤️ 찜 토글 (FIXED)
    ========================= */
    @Transactional
    public ProductLikeResultVO toggleLike(Long productId, Long userId) {

        boolean liked;

        // ✅ userId, productId 순서가 핵심
        if (productLikeMapper.exists(userId, productId)) {
            productLikeMapper.delete(userId, productId);
            liked = false;
        } else {
            productLikeMapper.insert(userId, productId);
            liked = true;
        }

        int likeCount = productLikeMapper.countByProductId(productId);

        return new ProductLikeResultVO(
                String.valueOf(productId),
                likeCount,
                liked
        );
    }
}
