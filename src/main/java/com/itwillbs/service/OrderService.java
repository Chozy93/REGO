package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
    @Value("${iamport.imp_key}")
    private String apiKey;

    @Value("${iamport.imp_secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();
	
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
    
    
    // ✅ 액세스 토큰 발급
    public String getAccessToken() {

        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", apiKey);
        body.put("imp_secret", apiSecret);

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<IamportResponse> response =
                restTemplate.postForEntity(url, entity, IamportResponse.class);

        Map responseBody = (Map) response.getBody().getResponse();
        return (String) responseBody.get("access_token");
    }
    // iamport 직접 발급받기
    public Payment getPaymentIncludeSandbox(String impUid) {
        String accessToken = getAccessToken(); // 기존 토큰 발급 로직

        String url = "https://api.iamport.kr/payments/" + impUid + "?include_sandbox=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<IamportResponse<Payment>> response =
            restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
            );

        return response.getBody().getResponse();
    }


    // 거래완료 버튼 눌렀을 때 product 상태 변경하기
 // 거래완료 버튼 눌렀을 때 product 상태 변경하기
    @Transactional
    public void confirmProductStatus(Long productId, Long buyerId) {
        // 1. 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 2. 권한 검증 (상품에 저장된 구매자 객체에서 ID를 꺼내 비교)
        if (product.getBuyer() == null || !product.getBuyer().getUserId().equals(buyerId)) {
            throw new IllegalStateException("본인이 구매 확정한 상품이 아니거나 구매자 정보가 없습니다.");
        }

        // 3. 상태 검증 (RESERVED 상태일 때만 SOLD로 변경 가능)
        // Enum을 사용 중이시라면 ProductSalesStatus.RESERVED와 비교하세요.
        if (product.getSalesStatus() != ProductSalesStatus.RESERVED) {
            throw new IllegalStateException("예약 중인 상품만 거래 완료 처리가 가능합니다.");
        }

        // 4. 상태 변경
        // product가 이미 가지고 있는 buyer 객체를 다시 넣어주면서 상태만 SOLD로 변경
        product.changeSalesStatus(ProductSalesStatus.SOLD, product.getBuyer());
        
        // 💡 별도의 save()를 호출하지 않아도 @Transactional 덕분에 더티 체킹으로 DB에 반영됩니다.
    }
    
}
