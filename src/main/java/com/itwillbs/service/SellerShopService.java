package com.itwillbs.service;

import com.itwillbs.dto.SellerShopProductListDTO;
import com.itwillbs.mapper.SellerShopMapper;
import com.itwillbs.view.SellerShopPageVO;
import com.itwillbs.view.SellerShopProductCardVO;

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

        List<SellerShopProductListDTO> list =
                sellerShopMapper.selectSellerProducts(sellerId);

        if (list == null || list.isEmpty()) {
            return SellerShopPageVO.empty(sellerId);
        }

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

        return new SellerShopPageVO(
                sellerId != null ? sellerId.toString() : "",
                cards
        );
    }
}
