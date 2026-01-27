package com.itwillbs.dto;

import java.util.List;

import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.SubCategoryVO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductByCategoryResponse {

    private String parentCategoryName;   // 버튼에 표시할 대분류명
    private List<SubCategoryVO> subCategories; // 소분류 목록
    private List<MainProductCardVO> products;
       // 🔥 상품 카드 HTML
}
