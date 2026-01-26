package com.itwillbs.service;

import com.itwillbs.domain.ProductVO; // VO 임포트!
import com.itwillbs.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

    public Page<ProductVO> getPurchaseHistory(Long userId, Pageable pageable) {
        return productRepository.findByBuyer_UserId(userId, pageable)
                                .map(ProductVO::new); 
    }
}