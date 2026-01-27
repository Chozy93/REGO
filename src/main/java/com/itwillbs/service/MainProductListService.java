package com.itwillbs.service;

import com.itwillbs.dto.MainProductListDTO;
import com.itwillbs.mapper.MainProductMapper;
import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.condition.MainProductSortConditionVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainProductListService {
	
	private static final int POPULAR_LIMIT = 12;

    private final MainProductMapper mainProductMapper;

  
    /* =========================
       최근 등록 상품
       MAIN01_LIST
    ========================= */
    public List<MainProductCardVO> getRecentProducts(Long userId) {
        List<MainProductListDTO> list = mainProductMapper.selectRecentProducts(userId);
        return list.stream().map(this::toCardVO).toList();
    }
    
    /* =========================
    최근 등록 상품 (정렬)
    MAIN01_SORT_ORDER
 ========================= */
    public List<MainProductCardVO> getRecentProducts(Long userId, MainProductSortConditionVO condition, String region) {
        List<MainProductListDTO> list =
                mainProductMapper.selectRecentProductsWithSort(userId, condition.getSort(), region);
        return list.stream().map(this::toCardVO).toList();
    }

    /* =========================
     * 인기 상품
     * MAIN01_POPULAR
     ========================= */
    public List<MainProductCardVO> getPopularProducts(Long userId, String region)
 {
        List<MainProductListDTO> list =
                mainProductMapper.selectPopularProducts(userId, POPULAR_LIMIT, region);
        return list.stream().map(this::toCardVO).toList();
    }

    /* =========================
       DTO → 카드 VO 변환
       (메인 찜 동기화 핵심)
    ========================= */
    private MainProductCardVO toCardVO(MainProductListDTO dto) {

        return new MainProductCardVO(
                String.valueOf(dto.getProductId()),
                dto.getTitle(),
                dto.getPrice(),
                dto.getThumbnailUrl(),
                dto.getRegionName(),
                dto.getCreatedTime(),
                false,                 // reserved (아직 미구현)
                dto.getLikeCount(),    // ❤️ DB 기준
                dto.isLiked()          // ❤️ 로그인 사용자 기준
        );
    }
}
