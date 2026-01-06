package com.itwillbs.service;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.view.ProductDetailPageVO;
import com.itwillbs.view.ProductDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductService productService;
    private final ProductLikeService productLikeService;
    // ⚠️ 현재는 더미 DTO 제공용
    // 추후 MyBatis Mapper로 교체 예정

    /* =========================
       상품 상세 페이지 조회
       DETAIL01_INFO
       DETAIL01_STATUS
    ========================= */
    public ProductDetailPageVO getProductDetailPage(Long productId) {

        ProductDetailDTO dto =
                productService.getProductDetail(productId);

        if (dto == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다. id=" + productId);
        }

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
                dto.getViewCount(),
                dto.getLikeCount(),
                dto.getChatCount(),
                dto.isLiked(),
                dto.getCreatedAtDisplay()
        );

        // 🔥 메인 ↔ 상세 찜 상태 동기화
        boolean liked =
                productLikeService.isLiked(productId, "testUser");
        int likeCount =
                productLikeService.getLikeCount(productId);

        productVO.setLiked(liked);
        productVO.setLikeCount(likeCount);

        return new ProductDetailPageVO(productVO);
    }

    /* =========================
       null 방어 (헌법 규칙)
    ========================= */
    private List<String> safeList(List<String> list) {
        return list != null ? list : Collections.emptyList();
    }
}
