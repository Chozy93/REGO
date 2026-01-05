package com.itwillbs.service;

import com.itwillbs.dto.MainProductListDTO;
import com.itwillbs.mapper.MainProductMapper;
import com.itwillbs.view.MainProductCardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainProductListService {

    private final MainProductMapper mainProductMapper;

    /* =========================
       최근 등록 상품 조회
    ========================= */
    public List<MainProductCardVO> getRecentProducts() {

        List<MainProductListDTO> dtoList =
                mainProductMapper.selectRecentProducts();

        return dtoList == null
                ? List.of()
                : dtoList.stream()
                         .map(this::toCardVO)
                         .toList();
    }

    /* =========================
       인기 상품 조회
    ========================= */
    public List<MainProductCardVO> getPopularProducts() {

        List<MainProductListDTO> dtoList =
                mainProductMapper.selectPopularProducts();

        return dtoList == null
                ? List.of()
                : dtoList.stream()
                         .map(this::toCardVO)
                         .toList();
    }

    /* =========================
       AI 추천 상품 (임시)
       ※ 추후 실제 로직으로 교체 예정
    ========================= */
    public List<MainProductCardVO> getAiProducts() {

        return List.of(); // AI 추천 미구현 상태 → 빈 리스트
    }

    /* =========================
       DTO → View 전용 VO 변환
    ========================= */
    private MainProductCardVO toCardVO(MainProductListDTO dto) {

        String imageUrl =
                (dto.getMainImageUrl() == null || dto.getMainImageUrl().isBlank())
                        ? "/images/temp/product-default.png"
                        : dto.getMainImageUrl();

        return new MainProductCardVO(
                dto.getProductId().toString(),   // VO ID는 String
                dto.getProductName(),
                dto.getPrice(),
                imageUrl,
                dto.getRegionDisplayName(),
                "방금 전",                       // 날짜 포맷은 추후 교체
                false,                         // 예약 여부 임시
                false                          // ❤️ liked (STEP 2-③ 핵심)
        );
    }
}
