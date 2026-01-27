package com.itwillbs.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.itwillbs.view.MainProductCardVO;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIRecommendProductService {

    private final AIRecommendProductMapper aiRecommendProductMapper;

    public List<MainProductCardVO> getRecommend(String recentIds) {

        if (recentIds == null || recentIds.isBlank()) {
            return Collections.emptyList();
        }

        return aiRecommendProductMapper.selectAIProducts()
                .stream()
                .map(dto -> new MainProductCardVO(
                        String.valueOf(dto.getProductId()),
                        dto.getTitle(),
                        dto.getPrice(),
                        dto.getThumbnail(),
                        null,   // location
                        null,   // time
                        false,  // reserved
                        0,      // likeCount
                        false   // liked
                ))
                .toList();
    }
}
