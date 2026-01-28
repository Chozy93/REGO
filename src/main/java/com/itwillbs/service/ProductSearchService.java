package com.itwillbs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.ProductVO;
import com.itwillbs.dto.ProductSearchDTO;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.enumtype.ProductSalesStatus;
import com.itwillbs.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSearchService {
	private final ProductRepository productRepository;

	
	// 검색하기
	public Page<ProductVO> search(ProductSearchDTO cond, Pageable pageable) {
	    String keyword = (cond.getKeyword() != null) ? cond.getKeyword() : "";
	    
	    // 수정된 메서드 호출
	    Page<Product> result = productRepository.findByKeyword(keyword, pageable);
	    
	    return result.map(ProductVO::new);
	}
}
