package com.itwillbs.service;

import com.itwillbs.dto.SellerInfoDTO;
import com.itwillbs.mapper.SellerInfoMapper;
import com.itwillbs.view.SellerInfoVO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerInfoService {

    private final SellerInfoMapper sellerInfoMapper;

    public SellerInfoVO getSellerInfo(Long productId) {

        SellerInfoDTO dto =
                sellerInfoMapper.selectSellerInfoByProductId(productId);

        if (dto == null) {
            throw new IllegalArgumentException("판매자 정보가 없습니다.");
        }

        // ✅ 여기서 sellerBadge 판단
        String sellerBadge =
                dto.getJoinedAt() != null &&
                dto.getJoinedAt().isAfter(LocalDateTime.now().minusDays(30))
                        ? "신규 판매자"
                        : "일반 판매자";

        // ✅ 판단 결과만 VO에 전달
        return new SellerInfoVO(
                dto.getSellerId(),     // ← 상점 이동 핵심
                dto.getNickname(),
                sellerBadge,
                dto.getSellingCount(),
                dto.getSoldCount()
        );
    }
}
