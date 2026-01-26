package com.itwillbs.service;

import com.itwillbs.dto.SellerShopProductListDTO;

import com.itwillbs.mapper.SellerShopMapper;
import com.itwillbs.view.SellerShopPageVO;
import com.itwillbs.view.SellerShopProductCardVO;
import com.itwillbs.view.SellerShopSellerProfileVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerShopService {

    private final SellerShopMapper sellerShopMapper;

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SellerShopPageVO getSellerShopPage(Long sellerId) {

        /* =========================
           1️⃣ 판매자 프로필 조회 (VO 그대로)
        ========================= */
        SellerShopSellerProfileVO sellerProfile =
                sellerShopMapper.selectSellerProfile(sellerId);

        /* =========================
           2️⃣ 판매자 상품 목록 조회
        ========================= */
        List<SellerShopProductListDTO> list =
                sellerShopMapper.selectSellerProducts(sellerId);

        List<SellerShopProductCardVO> cards = list.stream()
                .map(dto -> new SellerShopProductCardVO(
                        dto.getProductId(),
                        dto.getProductName(),
                        dto.getPrice(),
                        dto.getMainImageUrl(),
                        dto.getRegionDisplayName(),
                        dto.getCreatedAt() != null
                                ? dto.getCreatedAt().format(TIME_FMT)
                                : "",
                        dto.getLikeCount(),
                        dto.getViewCount()
                ))
                .toList();

        /* =========================
           3️⃣ Page VO 조립
        ========================= */
        return new SellerShopPageVO(
                sellerId,
                sellerProfile,
                cards
        );
    }
}
