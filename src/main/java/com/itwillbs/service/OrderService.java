package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.dto.OrderRequestDTO;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.ProductOrder;
import com.itwillbs.entity.User;
import com.itwillbs.entity.UserAddress;
import com.itwillbs.entity.enumtype.ProductSalesStatus;
import com.itwillbs.mapper.OrderMapper;
import com.itwillbs.repository.OrderRepository;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.repository.UserAddressReopsitory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final ProductRepository productRepository;
	private final UserAddressReopsitory addressRepository;
	private final PaymentService paymentService;
	private final OrderMapper orderMapper;
	private final OrderRepository orderRepository;

	
	// ---------- 바로 결제 ---------- 
	
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
    
    // ----- 바로 결제 주문 생성하기 ---
    @Transactional
    public Long createOrder(OrderRequestDTO dto, Long buyerId) {
        // 1. REPAY 결제인 경우 잔액 차감 로직 (예외 발생 시 롤백)
        if ("REPAY".equals(dto.getPaymentType())) {
            paymentService.processPayment(buyerId, dto.getTotalAmount(), dto.getProductId());
        }
        
        

        // 2. 주문 엔티티(Map) 구성
        // 공통 정보
        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("productId", dto.getProductId());
        orderParams.put("buyerId", buyerId);
        orderParams.put("sellerId", dto.getSellerId());
        orderParams.put("paymentType", dto.getPaymentType());
        orderParams.put("totalAmount", dto.getTotalAmount());
        orderParams.put("escrowStatus", "PENDING"); // 초기 상태 고정
        orderParams.put("dealType", "DELIVERY");    // 배송 결제 고정
        
        // 배송일 경우에만 상세 정보 저장
        if ("DELIVERY".equals(dto.getDealType())) {
            orderParams.put("receiverName", dto.getReceiverName());
            orderParams.put("receiverPhone", dto.getReceiverPhone());
            orderParams.put("zipCode", dto.getZipCode());
            orderParams.put("address", dto.getAddress());
            orderParams.put("addressDetail", dto.getAddressDetail());
            orderParams.put("deliveryRequest", dto.getFinalDeliveryMessage());
        }

        // 4. DB Insert
        orderMapper.insertOrder(orderParams);

        // 생성된 order_id 반환 (MyBatis의 selectKey 또는 generatedKeys 사용)
        return (Long) orderParams.get("orderId");
    }
    
    // 결제 후 product 상태 바꾸는 메서드
    @Transactional
    public void completeProductSales(Long orderId) {
        // 1. 주문서 조회 (주문서에 이미 buyer 정보와 product 정보가 매핑되어 있어야 함)
        ProductOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 내역을 찾을 수 없습니다."));

        // 2. 주문서에서 상품과 구매자(현재 결제한 사람) 추출
        Product product = order.getProduct();
        User buyer = order.getBuyer();

        // 3. 엔티티 메서드 실행 (상태 SOLD_OUT으로 변경 + 구매자 ID 세팅)
        product.changeSalesStatus(ProductSalesStatus.RESERVED, buyer);
        
        // 💡 별도의 save() 없이도 @Transactional에 의해 메서드 종료 시 자동 업데이트됨
    }
    
    
    // 결제 성공 후 product_orders 테이블 값 가져오는 로직
    public ProductOrder getOrderById(Long orderId) {
        return orderRepository.findByIdWithProduct(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다. ID: " + orderId));
    }
    
    
}
