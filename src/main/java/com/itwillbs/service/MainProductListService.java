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

        return mainProductMapper.selectMainProductList()
                .stream()
                .map(dto -> new MainProductCardVO(
                        dto.getProductId(),
                        dto.getProductName(),
                        dto.getPrice(),
                        dto.getMainImageUrl(),
                        dto.getRegionDisplayName(),
                        "방금 전",      // 임시
                        false           // 임시
                ))
                .toList();
    }
}

