package com.itwillbs.service;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.dto.ProductSellerInfoDTO;
import com.itwillbs.mapper.ProductDetailMapper;
import com.itwillbs.view.ProductDetailPageVO;
import com.itwillbs.view.ProductDetailVO;
import com.itwillbs.view.ProductSimilarListVO;
import com.itwillbs.view.ProductSellerInfoVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailMapper productDetailMapper;
    private final ProductLikeService productLikeService;
    private final ProductSimilarQueryService productSimilarQueryService;
    private final ProductReportService productReportService;

    /* =========================
       상품 상세 페이지 조회 (비로그인)
    ========================= */
    public ProductDetailPageVO getProductDetailPage(Long productId) {
        return getProductDetailPage(productId, false, null);
    }

    /* =========================
       상품 상세 페이지 조회 (공통)
    ========================= */
    public ProductDetailPageVO getProductDetailPage(
            Long productId,
            boolean increaseView,
            Long loginUserId
    ) {

        /* =========================
           0️⃣ 로그인 여부
        ========================= */
        boolean isLogin = (loginUserId != null);

        /* =========================
           1️⃣ 조회수 증가
        ========================= */
        if (increaseView) {
            productDetailMapper.increaseViewCount(productId);
        }

        /* =========================
           2️⃣ 상품 기본 정보
        ========================= */
        ProductDetailDTO dto =
                productDetailMapper.selectProductDetail(productId);

        if (dto == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다. id=" + productId);
        }

        /* =========================
           3️⃣ 상세 이미지
        ========================= */
        List<String> imageUrls =
                productDetailMapper.selectProductImages(productId);

        if (imageUrls == null || imageUrls.isEmpty()) {
            if (dto.getMainImageUrl() != null && !dto.getMainImageUrl().isBlank()) {
                imageUrls = List.of(dto.getMainImageUrl());
            } else {
                imageUrls = Collections.emptyList();
            }
        }

        dto.setImageUrls(imageUrls);

        /* =========================
           4️⃣ 찜 정보
        ========================= */
        boolean liked = false;
        if (isLogin) {
            liked = productLikeService.isLiked(productId, loginUserId);
        }

        int likeCount =
                productLikeService.getLikeCount(productId);

        /* =========================
           5️⃣ 상품 VO
        ========================= */
        ProductDetailVO productVO =
                new ProductDetailVO(dto);
       

        productVO.setLiked(liked);
        productVO.setLikeCount(likeCount);

        /* =========================
           6️⃣ 판매자 정보 (STEP 6)
        ========================= */
        ProductSellerInfoVO sellerInfo =
                getSellerInfo(productId);

        /* =========================
           7️⃣ 신고 여부
        ========================= */
        boolean alreadyReported = false;
        if (isLogin) {
            alreadyReported =
                    productReportService.isAlreadyReported(productId, loginUserId);
        }

        /* =========================
           8️⃣ 비슷한 상품
        ========================= */
        ProductSimilarListVO similar =
                productSimilarQueryService.getSimilarProducts(productId, 4);

        /* =========================
           9️⃣ PageVO 조립 (최종)
        ========================= */
        return new ProductDetailPageVO(
                productVO,
                sellerInfo,
                similar,
                isLogin,
                alreadyReported
        );
    }

    /* =========================
       DETAIL01_SELLER_INFO
       판매자 정보 조회
    ========================= */
    public ProductSellerInfoVO getSellerInfo(Long productId) {

        ProductSellerInfoDTO dto =
                productDetailMapper.selectSellerInfo(productId);

        if (dto == null) {
            return new ProductSellerInfoVO(
                    new ProductSellerInfoDTO()
            );
        }

        return new ProductSellerInfoVO(dto);
    }
}
