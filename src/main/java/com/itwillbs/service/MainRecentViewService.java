package com.itwillbs.service;

import com.itwillbs.mapper.MainRecentViewMapper;
import com.itwillbs.view.MainProductCardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainRecentViewService {

    private final MainRecentViewMapper mapper;

    /**
     * 최근 본 상품 조회
     * @param recentIds localStorage에서 전달된 "1,2,3" 형태 문자열
     */
    public List<MainProductCardVO> getRecentView(String recentIds) {

        // 1️⃣ 입력값 방어
        if (recentIds == null || recentIds.isBlank()) {
            return Collections.emptyList();
        }

        // 2️⃣ String → List<Long>
        List<Long> ids = Arrays.stream(recentIds.split(","))
                               .map(Long::valueOf)
                               .toList();

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        // 3️⃣ DB 조회 (순서 유지)
        return mapper.selectRecentViewProducts(ids);
    }
}
