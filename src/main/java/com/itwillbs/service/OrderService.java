package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.Product;
import com.itwillbs.entity.UserAddress;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.repository.UserAddressReopsitory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final ProductRepository productRepository;
	private final UserAddressReopsitory addressRepository;

	
	// ---------- 바로 결제 
	
	// 상품 상세 정보 가져오기
    public Product getProductById(Long productId) {
        // findById는 Optional을 반환하므로 .orElseThrow를 통해 
        // 데이터가 없을 때의 처리를 한 줄로 끝낼 수 있습니다.
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + productId));
    }
    
    // 구매하려는 유저의 address 정보 가져오기
    public UserAddress getDefaultAddress(Long userId) {
        // 기본 배송지를 조회하되, 없으면 null을 반환하거나 빈 객체를 반환
        return addressRepository.findByUserUserIdAndIsDefaultTrue(userId)
                .orElse(null); 
    }
    
    
}
