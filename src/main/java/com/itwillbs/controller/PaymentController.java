package com.itwillbs.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwillbs.config.FintechProperties;
import com.itwillbs.dto.MyPageDTO;
import com.itwillbs.dto.PayOAuthTokenResponseDTO;
import com.itwillbs.dto.PaymentRequestDto;
import com.itwillbs.dto.WalletTransactionDTO;
import com.itwillbs.dto.WalletViewDTO;
import com.itwillbs.mapper.MypageMapper;
import com.itwillbs.service.ChatPaymentService;
import com.itwillbs.service.OpenBankingAuthService;
import com.itwillbs.service.PaymentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


// 결제 제어 컨트롤러
@RequiredArgsConstructor
@Controller
public class PaymentController {
	
	private final FintechProperties fintechProperties;
	// API 인증을 위한 서비스 주입
	private final OpenBankingAuthService openBankingAuthService;
	// mypage에서 user정보 가져온거 재활용
	final public MypageMapper mypageMapper;
	//DB 저장을 위해 서비스 주입 
    private final PaymentService paymentService;
    private final ChatPaymentService chatPaymentService;
    
  


	
	// 마이페이지 -> re:pay 결제 관리 페이지로 이동
    // 지갑 + 계좌 연동 상태에 따라 화면 다르게 출력
    @GetMapping("/myrepay")
    public String myRepayPayments(Authentication authentication, Model model) {
    	
    	// 1. 이메일 추출 (아래 만들어둔 메서드 사용)
        String email = getUserEmail(authentication);
     // 이메일이 없으면 로그인으로
        if(email == null) return "redirect:/login";
        System.out.println("email = " + email);
        
     // 2. 이미 있는 MypageMapper로 유저 정보 가져오기!
        MyPageDTO user = mypageMapper.getMyPageInfo(email);
        System.out.println(user);
        
       // 3. 가져온 정보에서 ID(PK)만 쏙 빼서 지갑 조회에 사용
        WalletViewDTO walletInfo = paymentService.getMyWalletPage(user.getUserId());
        //  화면(HTML)으로 정보 전달
        model.addAttribute("walletInfo", walletInfo);
    	System.out.println(walletInfo);
    	
    	// 전체 계좌 목록을 가져와서 화면에 뿌림 (modal용)
        List<WalletViewDTO> accountList = paymentService.getAccountList(user.getUserId());
        model.addAttribute("accountList", accountList);
        
        // 거래 내역 조회 및 모델 저장
        List<WalletTransactionDTO> historyList = paymentService.getHistoryList(user.getUserId());
        model.addAttribute("historyList", historyList); // HTML에서 이 이름("historyList")을 씀
        System.out.println(historyList);
        
        return "payment/myrepay";
    }
	
    
    
    // --------------------- 가상 계좌 연동하기(맨 처음에 wallet 생성) ---------------------------
    // 계좌 연동하기 버튼 클릭 시 금융결제원 OAuth 실행 
    // ----- access_token (은행 API 호출용), refresh_token(토큰 갱신용), user_seq_no(사용자 식별자) 발급받기
    // 저장된 토큰으로 1)잔액 조회 2)가상 충전 3)결제 처리까지 
    
    
    // 1. 계좌 연동 요청 (금융결제원 로그인 페이지로 리다이렉트) ->  계좌 연동하기 버튼 눌렀을 때 
    @GetMapping("/pay/connect")
    public void connect(HttpServletResponse response) throws IOException {
    	String url = fintechProperties.getBaseUrl()
    	           + "/oauth/2.0/authorize"
    	           + "?response_type=code"
    	           + "&client_id=" + fintechProperties.getClientId()
    	           + "&redirect_uri=" + fintechProperties.getRedirectUri()
    	           + "&scope=login inquiry transfer" // [추가] 권한 범위 명시 (조회, 이체 등)
    	           + "&state=12345678901234567890123456789012"
    	           + "&auth_type=0"; // 0:최초인증, 1:재인증(선택)

        response.sendRedirect(url);
    }
    

    
 // ---- 금융 결제원에서 받아온 코드를 담아 이 주소로 리다이렉트 [계좌 연동]
    //  사용자가 로그인을 완료하면, redirect url로 인증코드 받아오기 
    // 이 코드를 받아서 진짜 토큰으로 바꾸고 DB에 저장
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, 
    						Authentication auth) {
        
    	//1.  로그인 안 된 상태면 튕겨내기
        if(auth == null) return "redirect:/login";
        System.out.println("1. 인가 코드(Code) 수신 완료: " + code);
        
        

        // 2. 받아온 code로 금융결제원에서 User_seq_no 받아오기
        PayOAuthTokenResponseDTO tokenResponse = openBankingAuthService.requestToken(code);
        
        System.out.println("2. Access Token 발급 완료: " + tokenResponse.getAccess_token());
        System.out.println("3. 사용자 일련번호(user_seq_no): " + tokenResponse.getUser_seq_no());

        // 3. 여기서도 똑같이 Mapper 재사용!
        String email = getUserEmail(auth);
        MyPageDTO user = mypageMapper.getMyPageInfo(email);

        // 4. 가져온 ID로 계좌 등록
        paymentService.registerAccount(user.getUserId(), tokenResponse);
       // 이미 지갑이 있는지 확인
        boolean hasWallet = paymentService.hasWallet(user.getUserId());
        if (!hasWallet) {
            // 지갑이 없으면 -> 비밀번호 설정 페이지로 이동!
            return "redirect:/pay/setup-password"; 
        }
        
        
        // 지갑이 이미 있으면 -> re:pay 관리 페이지로 이동
        return "redirect:/myrepay"; 
    }
    
    
  //  비밀번호 설정 페이지 보여주기
    @GetMapping("/pay/setup-password")
    public String setupPasswordPage() {
        return "payment/setup_password"; 
    }
    
   // 비밀번호 입력받아 지갑 생성하기
    @PostMapping("/pay/create-wallet")
    public String createWallet(@RequestParam("payPassword") String payPassword, Authentication authentication) {
    	// 2. 이메일 추출 (만들어두신 getUserEmail 메서드 활용)
        String email = getUserEmail(authentication);
     // 3. Mapper를 통해 확실한 유저 정보 가져오기 (보여주신 코드와 동일)
        MyPageDTO user = mypageMapper.getMyPageInfo(email);
        // 지갑 생성 서비스 호출 (비밀번호 전달)
        paymentService.createWallet(user.getUserId(), payPassword);
        
        return "redirect:/myrepay"; // 생성 완료 후 re:pay 페이지
    }
    

    
    

    // -----------------[주계좌]  
    // 주계좌 변경 요청 처리
    @PostMapping("/pay/change-main")
    public String changeMainAccount(@RequestParam("accountId") Long accountId, 
                                    Authentication authentication) {
        String email = getUserEmail(authentication);
        MyPageDTO user = mypageMapper.getMyPageInfo(email);
        
        // 서비스 호출해서 주계좌 변경
        paymentService.changeMainAccount(user.getUserId(), accountId);
        
        return "redirect:/myrepay"; // 변경 후 다시 관리 페이지로
    }
    
    
 // [버튼 클릭] 계좌 연동 해지 처리
    @PostMapping("/pay/delete-account")
    public String deleteAccount(@RequestParam("accountId") Long accountId, 
                                Authentication authentication, 
                                RedirectAttributes redirectAttributes) {
    	
    	// 로그로 accountId가 실제로 들어오는지 확인
        System.out.println("🚩 삭제 요청 받은 accountId: " + accountId);
        String email = getUserEmail(authentication);
        MyPageDTO user = mypageMapper.getMyPageInfo(email);
        
        try {
            // 서비스에서 금결원 API 호출 및 DB 삭제 동시 처리
            paymentService.deleteAccount(user.getUserId(), accountId);
            redirectAttributes.addFlashAttribute("message", "계좌 연동이 성공적으로 해지되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "계좌 해지 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return "redirect:/myrepay";
    }
    
    
   // [버튼 클릭] 충전하기 
    @PostMapping("/pay/charge")
    public String chargePoint(@RequestParam("amount") String amountStr, Authentication authentication) {
    	String email = getUserEmail(authentication);
        MyPageDTO user = mypageMapper.getMyPageInfo(email);

        // 1. 콤마(,) 제거 후 숫자로 변환
        Long amount = Long.parseLong(amountStr.replace(",", ""));
       // 2. 서비스 로직 실행
        paymentService.chargePoint(user.getUserId(), amount);
        
        return "redirect:/myrepay"; // 충전 후 다시 마이페이지로 이동
    }
    
    // [버튼 클릭] 출금하기 (내 지갑 -> 주계좌) 
    @PostMapping("/pay/withdraw")
    public String withdrawPoint(@RequestParam("amount") String amountStr, Authentication authentication) {
    	String email = getUserEmail(authentication);
        MyPageDTO user = mypageMapper.getMyPageInfo(email);

        // 1. 콤마(,) 제거 후 숫자로 변환
        Long amount = Long.parseLong(amountStr.replace(",", ""));
       // 2. 서비스 로직 실행
        paymentService.processWithdraw(user.getUserId(), amount);
        
        return "redirect:/myrepay"; // 충전 후 다시 마이페이지로 이동
    }
    
    
    
    
    
    
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
    

    
    
    // email값 가져오기
    private String getUserEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            Map<String, Object> attributes = ((OAuth2User) authentication.getPrincipal()).getAttributes();
            if(attributes.get("email") != null) {
                return (String) attributes.get("email");
            } else if (attributes.get("response") != null) {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("email");
            }
        }
        return null;
    }
    
    
}
