package com.itwillbs.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.dto.PaymentRequestDto;
import com.itwillbs.dto.WalletViewDTO;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.User;
import com.itwillbs.entity.UserAddress;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.service.ChatPaymentService;
import com.itwillbs.service.OrderService;
import com.itwillbs.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final PaymentService paymentService;
	
	private final ChatPaymentService chatPaymentService;
	private final OrderService orderService;
	

	
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
 
        return "payment/direct-pay"; // 작성하신 HTML 경로
    }
    
    
}
