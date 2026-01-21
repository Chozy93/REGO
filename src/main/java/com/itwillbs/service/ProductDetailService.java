package com.itwillbs.service;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.mapper.ProductDetailMapper;
import com.itwillbs.view.ProductDetailPageVO;
import com.itwillbs.view.ProductDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailMapper productDetailMapper;
    private final ProductLikeService productLikeService; // (다음 단계용)

    /* =========================
       상품 상세 페이지 조회
       DETAIL01_INFO
       DETAIL01_STATUS
       DETAIL01_VIEWCOUNT
    ========================= */
    public ProductDetailPageVO getProductDetailPage(Long productId) {
    	return getProductDetailPage(productId, true, null); 
    	
    }
    
    public ProductDetailPageVO getProductDetailPage(Long productId, boolean increaseView, Long userId) {

    	// ✅ 1. 조회수 증가 (조건부)
        if (increaseView) {
            productDetailMapper.increaseViewCount(productId);
        }

        // ✅ 2️⃣ 상품 상세 조회 (반드시 같은 Mapper)
        ProductDetailDTO dto =
                productDetailMapper.selectProductDetail(productId);

        if (dto == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다. id=" + productId);
        }
        

        boolean liked = (userId != null) && productLikeService.isLiked(productId, userId);
        int likeCount = productLikeService.getLikeCount(productId);

        ProductDetailVO productVO = new ProductDetailVO(
                dto.getProductId().toString(),
                dto.getProductName(),
                dto.getPrice(),
                dto.getPriceDisplay(),
                dto.getDescription(),
                safeList(dto.getImageUrls()),
                dto.getSellerNickname(),
                dto.getSellerRegion(),
                dto.getSellerTemperature() + "°",
                dto.getConditionLabel(),
                dto.getViewCount(),     // 🔥 DB 값 그대로
                likeCount,
                0,
                liked,          // (현재는 false or 더미)
                dto.getCreatedAtDisplay()
        );

        return new ProductDetailPageVO(productVO);
    }

    /* =========================
       null 방어 (헌법 규칙)
    ========================= */
    private List<String> safeList(List<String> list) {
        return list != null ? list : Collections.emptyList();
    }
}
