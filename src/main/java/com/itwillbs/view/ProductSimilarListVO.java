package com.itwillbs.view;

import java.util.List;

import com.itwillbs.dto.ProductSimilarListDTO;

import lombok.Getter;

@Getter
public class ProductSimilarListVO {

    private final List<ProductSimilarItemVO> items;

    public ProductSimilarListVO(List<ProductSimilarListDTO> dtoList) {
        this.items = dtoList != null
                ? dtoList.stream()
                         .map(ProductSimilarItemVO::new)
                         .toList()
                : List.of();
    }

}

