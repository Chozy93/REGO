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

    private final ProductDetailMapper productDetailMapper;

    public ProductSimilarListVO getSimilarProducts(Long productId, int limit) {

        List<ProductSimilarDTO> list =
                productDetailMapper.selectSimilarProducts(productId, limit);

        if (list == null || list.isEmpty()) {
            list = productDetailMapper.selectPopularProductsForSimilar(limit);
        }

        return new ProductSimilarListVO(list);
    }
}
