package com.itwillbs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.ProductVO; // VO 임포트!
import com.itwillbs.dto.ProductByCategoryResponse;
import com.itwillbs.entity.Product;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.view.CategoryPageVO;
import com.itwillbs.view.MainProductCardVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductListService productListService;


    
    public Page<ProductVO> getPurchaseHistory(Long userId, Pageable pageable) {
        return productRepository.findByBuyer_UserId(userId, pageable)
                                .map(ProductVO::new); 
    }
    
    public ProductByCategoryResponse getProductsByParent(Long parentId) {

        // 1️⃣ 카테고리 정보
        CategoryPageVO page =
            productCategoryService.getCategoryPage(parentId);

        // 2️⃣ 상품 목록 (기존 로직 재사용 👍)
        List<MainProductCardVO> products =
            productListService.getProductsByParent(parentId);

        // 3️⃣ ✅ HTML ❌ / 데이터만 반환
        return new ProductByCategoryResponse(
            page.getCategoryGroup().getParentCategoryName(),
            page.getCategoryGroup().getSubCategories(),
            products   // ← String productHtml ❌
        );
    }
    
    public Product findById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(
                "존재하지 않는 상품입니다. id=" + productId
            ));
    }
}

