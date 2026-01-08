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

    private final MainProductMapper mainProductMapper;

    // 🔥 로그인 전 임시 테스트 유저
    private static final Long TEST_USER_ID = 1L;

    /* =========================
       최근 등록 상품
       MAIN01_LIST
    ========================= */
    public List<MainProductCardVO> getRecentProducts() {

        List<MainProductListDTO> list =
                mainProductMapper.selectRecentProducts(TEST_USER_ID);

        return list.stream()
                .map(this::toCardVO)
                .toList();
    }
    
    /* =========================
    최근 등록 상품 (정렬)
    MAIN01_SORT_ORDER
 ========================= */
    public List<MainProductCardVO> getRecentProducts(
            MainProductSortConditionVO condition
    ) {
        List<MainProductListDTO> list =
                mainProductMapper.selectRecentProductsWithSort(
                        TEST_USER_ID,
                        condition.getSort()
                );

        return list.stream()
                .map(this::toCardVO)
                .toList();
    }


    /* =========================
     * 인기 상품
     * MAIN01_POPULAR
     ========================= */
    private static final int POPULAR_LIMIT = 12;

    public List<MainProductCardVO> getPopularProducts() {

        List<MainProductListDTO> list =
            mainProductMapper.selectPopularProducts(
                TEST_USER_ID,
                POPULAR_LIMIT
            );

        return list.stream()
            .map(this::toCardVO)
            .toList();
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
