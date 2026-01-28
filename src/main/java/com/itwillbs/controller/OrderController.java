package com.itwillbs.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.dto.OrderRequestDTO;
import com.itwillbs.dto.PaymentRequestDto;
import com.itwillbs.dto.WalletViewDTO;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.ProductOrder;
import com.itwillbs.repository.PaymentLogRepository;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.service.ChatPaymentService;
import com.itwillbs.service.OrderService;
import com.itwillbs.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	@Value("${iamport.imp_code}")
    private String impCode;

    private final PaymentService paymentService;
	
	private final ChatPaymentService chatPaymentService;
	private final OrderService orderService;
	private final IamportClient iamportClient;
	private final PaymentLogRepository paymentLogRepository;
	

	
 // ----------------- 채팅에서 결제 요청 --------------------
    
    
    
    /**
     * 1. 판매자가 결제 요청을 보냄
     * 요청 예시: POST /api/payment/request
     */
    @PostMapping("/api/payment/request")
    public ResponseEntity<?> createRequest(@RequestBody PaymentRequestDto dto) {
        try {
            Long orderId = chatPaymentService.createPaymentRequest(
                    dto.getProductId(),
                    dto.getSellerId(),
                    dto.getBuyerId(),
                    dto.getRoomId(),
                    dto.getAmount()
            );
            
            // 성공 시 생성된 orderId 반환 -> 프론트에서는 이 ID를 소켓 메시지에 담아 전송
            return ResponseEntity.ok(Map.of("orderId", orderId, "message", "결제 요청이 생성되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 2. 구매자가 결제 버튼을 클릭하여 실제 결제 진행
     * 요청 예시: POST /api/payment/execute
     */
    @PostMapping("/api/payment/execute")
    public ResponseEntity<?> executePayment(@RequestBody Map<String, Long> payload) {
        try {
            Long orderId = payload.get("orderId");
            // 실제 서비스에서는 세션이나 시큐리티에서 현재 로그인한 유저 ID를 가져와야 합니다.
            Long currentUserId = 1L; // 임시: 현재 로그인한 구매자 ID

            chatPaymentService.processRepayPayment(orderId, currentUserId);
            
            return ResponseEntity.ok("re:pay 결제가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    
    
    
    
    
    // -------------------- 바로 결제 ---------------------------------
    // 바로결제 클릭했을 때 상품정보, user 정보, wallet 정보 화면에 들고오기
    @GetMapping("/direct")
    public String showDirectPayment(@RequestParam("productId") Long productId, 
    								@AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {
    	
    	// 1. 로그인 여부 체크 (userDetails가 없으면 결제창 접근 불가)
        if (userDetails == null) {
            return "redirect:/login"; 
        }
        
    	// 2. 로그인한 유저 정보 추출
        Long userId = userDetails.getUserId();
        // [유저 정보] 세션에서 바로 가져오기
        model.addAttribute("userInfo", userDetails.getUser());
            
        
        // 2. [지갑 정보] DB에서 최신 잔액 조회
        WalletViewDTO walletInfo = paymentService.getMyWalletPage(userId);
        model.addAttribute("walletInfo", walletInfo);
       
        // 3. 상품 정보 조회 
        Product product = orderService.getProductById(productId);
        model.addAttribute("product", product);
        // 아임퐤트 식별코드를 모델에 추가
        model.addAttribute("impCode", impCode);
 
        return "payment/direct-pay"; // 작성하신 HTML 경로
    }
    
    
    // 바로결제 클릭시 주문 생성
    @PostMapping("/direct")
    public String processOrder(@ModelAttribute OrderRequestDTO checkoutDTO,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        Payment payment = null;
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Long buyerId = userDetails.getUserId();

            if ("CARD".equals(checkoutDTO.getPaymentType())) {
                // 1. 액세스 토큰 발급 (SDK 기능을 활용해 토큰만 가져옵니다)
                String accessToken = iamportClient.getAuth().getResponse().getToken();
                
                // 2. [핵심] include_sandbox=true 파라미터를 붙여 직접 API 호출
                String url = "https://api.iamport.kr/payments/" + checkoutDTO.getImpUid() + "?include_sandbox=true";
                
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", accessToken); // 헤더에 토큰 설정
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    // JSON 결과에서 결제 정보 수동 파싱
                    JsonNode root = objectMapper.readTree(response.getBody());
                    JsonNode resNode = root.path("response");
                    
                    if (resNode.isMissingNode()) {
                        throw new RuntimeException("결제 정보를 찾을 수 없습니다 (포트원 응답 오류).");
                    }

                    // 금액 검증을 위해 수동으로 값 확인
                    long amount = resNode.path("amount").asLong();
                    String impUid = resNode.path("imp_uid").asText();
                    String merchantUid = resNode.path("merchant_uid").asText();

                    Product product = orderService.getProductById(checkoutDTO.getProductId());
                    
                    // 3. 위변조 검증
                    if (amount != product.getPrice()) {
                        iamportClient.cancelPaymentByImpUid(new CancelData(checkoutDTO.getImpUid(), true));
                        throw new RuntimeException("결제 금액 불일치! 위변조가 의심되어 취소되었습니다.");
                    }
                    
                    // 로그 저장을 위해 필요한 정보만 추출 (Payment 객체 흉내내기)
                    payment = new Payment(); 
                    // 참고: Payment 클래스의 세터가 열려있지 않다면 아래 로그 저장 로직에서 
                    // resNode에서 꺼낸 값을 직접 PgPaymentLog에 넣으세요.
                } else {
                    throw new RuntimeException("포트원 서버 통신 실패 (Status: " + response.getStatusCode() + ")");
                }
            }

            // 4. 주문 생성
            Long orderId = orderService.createOrder(checkoutDTO, buyerId);
            
            // 5. 로그 저장 (resNode에서 직접 꺼내는 방식으로 수정 권장)
            if ("CARD".equals(checkoutDTO.getPaymentType())) {
                // 로그 저장 로직... (이 부분은 사용자님의 PgPaymentLog 구조에 맞춰 직접 값을 넣으세요)
            }
            
            return "redirect:/direct/success/" + orderId;

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "결제 검증 중 오류: " + e.getMessage());
            return "redirect:/direct?productId=" + checkoutDTO.getProductId();
        }
    }
    
    // 바로결제 성공 화면
    @GetMapping("/direct/success/{orderId}")
    public String orderSuccess(@PathVariable("orderId") Long orderId, 
                               Model model) {
    	
    	// 상품 상태를 변경하고 구매자를 등록하는 코드
    	orderService.completeProductSales(orderId);
    	
        // 주문 정보를 DB에서 조회 (상품 정보와 연관관계가 맺어져 있어야 함)
        // 예: Order 엔티티 내부에 Product 정보가 포함된 형태
    	// 화면에 보여줄 주문 상세 정보 조회용 코드
        ProductOrder order = orderService.getOrderById(orderId); 
        
        // HTML에 "order"라는 이름으로 전달
        model.addAttribute("order", order);
        
        // 아까 만든 templates/payment/success.html 경로 반환
        return "payment/direct-success"; 
    }
    
    
 // 2. [결제 상세 확인 페이지] 
    @GetMapping("/order/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Long orderId, 
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        // 본인 주문인지 확인하는 로직 추가 권장
        ProductOrder order = orderService.getOrderById(orderId);
        
        if (order == null) return "redirect:/";
        
        model.addAttribute("order", order);
        return "payment/order-detail"; // 질문하신 이미지의 상세 레이아웃 페이지
    }
    
    
    
    // 거래 완료 버튼 눌렀을 때 product 상태 SOLD로 바꾸끼
    @PostMapping("/mypage/purchase/confirm/{productId}")
    @ResponseBody
    public ResponseEntity<String> confirmPurchase(
        @PathVariable("productId") Long productId,
        @AuthenticationPrincipal CustomUserDetails userDetails // 로그인한 유저 정보
    ) {
        try {
            Long buyerId = userDetails.getUserId();
            // 서비스에서 상태 변경 처리
            orderService.confirmProductStatus(productId, buyerId);
            return ResponseEntity.ok("거래가 완료되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류가 발생했습니다.");
        }
    }
    
    
}
