package com.itwillbs.service;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.mapper.ProductDetailMapper;
import com.itwillbs.view.ProductDetailPageVO;
import com.itwillbs.view.ProductDetailVO;
import com.itwillbs.view.ProductSimilarListVO;
import com.itwillbs.view.SellerInfoVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailMapper productDetailMapper;
    private final ProductLikeService productLikeService; // (다음 단계용)
    private final SellerInfoService sellerInfoService;
    private final ProductSimilarQueryService productSimilarQueryService;


    /* =========================
       상품 상세 페이지 조회
       DETAIL01_INFO
       DETAIL01_STATUS
       DETAIL01_VIEWCOUNT
    ========================= */
    public ProductDetailPageVO getProductDetailPage(Long productId) {
        return getProductDetailPage(productId, true, false);
    }

    
    public ProductDetailPageVO getProductDetailPage(
            Long productId,
            boolean increaseView,
            boolean isLogin
    ) {
        // 1️⃣ 조회수 증가
        if (increaseView) {
            productDetailMapper.increaseViewCount(productId);
        }

        // 2️⃣ 상품 상세 조회
        ProductDetailDTO dto =
                productDetailMapper.selectProductDetail(productId);

        if (dto == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다. id=" + productId);
        }

        // 3️⃣ 찜 정보 (임시 로그인 기준)
        Long testUserId = 1L; // 🔥 임시 테스트 유저
        boolean liked = isLogin && productLikeService.isLiked(productId, testUserId);
        int likeCount = productLikeService.getLikeCount(productId);

        // 4️⃣ ProductDetailVO 생성
        ProductDetailVO productVO = new ProductDetailVO(
                dto.getProductId().toString(),
                dto.getProductName(),
                dto.getPrice(),
                dto.getPriceDisplay(),
                dto.getDescription(),
                safeList(dto.getImageUrls()),
                dto.getSellerNickname(),
                dto.getSellerRegion(),
                dto.getSellerTemperature() + "",
                dto.getConditionLabel(),
                dto.getViewCount(),
                likeCount,
                0,
                liked,
                dto.getCreatedAtDisplay()
        );

        // 5️⃣ 판매자 정보
        SellerInfoVO seller =
                sellerInfoService.getSellerInfo(productId);

        // 6️⃣ 비슷한 상품
        ProductSimilarListVO similar =
                productSimilarQueryService.getSimilarProducts(productId, 4);

        
        // 8️⃣ PageVO 조립
        return new ProductDetailPageVO(
                productVO,
                seller,
                similar,
                isLogin
        );
    }

    /* =========================
       null 방어 (헌법 규칙)
    ========================= */
    private List<String> safeList(List<String> list) {
        return list != null ? list : Collections.emptyList();
    }
}
