package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.dto.ProductSimilarDTO;
import com.itwillbs.mapper.ProductDetailMapper;
import com.itwillbs.view.ProductSimilarListVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSimilarQueryService {

    private static final int DEFAULT_LIMIT = 4;

    private final ProductDetailMapper productDetailMapper;

    public ProductSimilarListVO getSimilarProducts(Long productId, int limit) {

        // 1️⃣ 같은 소분류 기준
        List<ProductSimilarDTO> result =
                productDetailMapper.selectSimilarBySubCategory(
                        productId,
                        limit
                );

        // 2️⃣ 부족하면 같은 대분류 fallback
        if (result.size() < limit) {

            int remain = limit - result.size();

            List<Long> excludeIds = result.stream()
                    .map(ProductSimilarDTO::getProductId)
                    .toList();

            List<ProductSimilarDTO> fallback =
                    productDetailMapper.selectSimilarByParentCategory(
                            productId,
                            remain,
                            excludeIds
                    );

            result.addAll(fallback);
        }

        return new ProductSimilarListVO(result);
    }

    // 편의 메서드 (limit 안 넘길 때)
    public ProductSimilarListVO getSimilarProducts(Long productId) {
        return getSimilarProducts(productId, DEFAULT_LIMIT);
    }
}

