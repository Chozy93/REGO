package com.itwillbs.service;

import com.itwillbs.dto.MainRecentViewDTO;
import com.itwillbs.mapper.MainRecentViewMapper;
import com.itwillbs.view.MainRecentViewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainRecentViewService {

    private final MainRecentViewMapper mapper;

    /**
     * 최근 본 상품 조회
     * @param recentIds localStorage에서 전달된 "1,2,3" 형태 문자열
     */
    public MainRecentViewVO getRecentView(String recentIds) {

        // 1️⃣ 입력 방어
        if (recentIds == null || recentIds.isBlank()) {
            return MainRecentViewVO.empty();
        }

        // 2️⃣ String → List<Long>
        List<Long> ids = Arrays.stream(recentIds.split(","))
                .map(Long::valueOf)
                .toList();

        if (ids.isEmpty()) {
            return MainRecentViewVO.empty();
        }

        // 3️⃣ DB 조회
        List<MainRecentViewDTO> dtos = mapper.selectRecentProducts(ids);

        // 4️⃣ VO 변환
        return new MainRecentViewVO(dtos);
    }
}
