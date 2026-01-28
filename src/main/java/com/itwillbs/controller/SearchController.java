package com.itwillbs.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.itwillbs.domain.ProductVO;
import com.itwillbs.dto.ProductSearchDTO;
import com.itwillbs.service.ProductSearchService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {
	private final ProductSearchService productSearchService;

	 // 검색하기 
	@GetMapping("/product/search")
    public String search(@ModelAttribute("searchDTO") ProductSearchDTO searchDTO,
                         @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {

        // 검색 결과 가져오기
        Page<ProductVO> productPage = productSearchService.search(searchDTO, pageable);

        model.addAttribute("products", productPage.getContent()); // 상품 리스트
        model.addAttribute("page", productPage);                 // 페이징 객체
        
        return "product/search"; // 아까 만든 HTML 파일명
    }
	

	
}
