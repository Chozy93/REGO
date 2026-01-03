package com.itwillbs.service;

import com.itwillbs.domain.MainProductCardVO;
import com.itwillbs.dto.MainProductListDTO;
import com.itwillbs.mapper.MainProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainProductListService {

    private final MainProductMapper mainProductMapper;

    public List<MainProductCardVO> getMainProductList() {

        List<MainProductListDTO> dtoList =
                mainProductMapper.selectMainProductList();

        if (dtoList.isEmpty()) {
            return List.of(
                new MainProductCardVO(
                    1L,
                    "아이패드 미니 6세대",
                    450000,
                    "https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?auto=format&fit=crop&w=800&q=80",
                    "서울 마포구",
                    "방금 전",
                    false
                ),
                new MainProductCardVO(
                    2L,
                    "27인치 게이밍 모니터",
                    180000,
                    "https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?auto=format&fit=crop&w=800&q=80",
                    "대구 수성구",
                    "5분 전",
                    true
                )
            );
        }

        return dtoList.stream()
                .map(dto -> new MainProductCardVO(
                        dto.getProductId(),
                        dto.getProductName(),
                        dto.getPrice(),
                        dto.getMainImageUrl(),
                        dto.getRegionDisplayName(),
                        "방금 전",
                        false
                ))
                .toList();
    }
}