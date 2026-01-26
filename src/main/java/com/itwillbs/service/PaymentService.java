package com.itwillbs.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.itwillbs.client.OpenBankingApiClient;
import com.itwillbs.dto.OpenBankingUserInfoResponseDTO;
import com.itwillbs.dto.PayOAuthTokenResponseDTO;
import com.itwillbs.dto.WalletTransactionDTO;
import com.itwillbs.dto.WalletViewDTO;
import com.itwillbs.entity.BankAccount;
import com.itwillbs.entity.PayWallet;
import com.itwillbs.entity.User;
import com.itwillbs.entity.WalletTransaction;
import com.itwillbs.entity.enumtype.PayTransactionType;
import com.itwillbs.entity.enumtype.TradeStatus;
import com.itwillbs.entity.enumtype.WalletStatus;
import com.itwillbs.mapper.PaymentMapper;
import com.itwillbs.repository.BankAccountRepository;
import com.itwillbs.repository.PayWalletRepository;
import com.itwillbs.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final UserRepository userRepository;
    private final PayWalletRepository payWalletRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentMapper paymentMapper;
    private final OpenBankingApiClient openBankingApiClient;
    private final OpenBankingAuthService openBankingService;
	
    
	 // 계좌 연동 및 지갑 생성 (Command -> JPA 사용) [DB에 저장하는 과정] -> 
	@Transactional
	public void registerAccount(Long userId, PayOAuthTokenResponseDTO tokenDto) {
	        
	        // 1. 사용자 조회
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	        
	      
	        
	     // 2. 클라이언트에 토큰값 전달해서 user의 계좌 정보 값 가져오기
	        OpenBankingUserInfoResponseDTO userInfo = openBankingApiClient.requestUserInfo(
	                tokenDto.getAccess_token(), 
	                tokenDto.getUser_seq_no()
	        );

	        // 3. 받아온 정보 DB 저장
	        if (userInfo != null && userInfo.getRes_list() != null) {
	            
	            for (OpenBankingUserInfoResponseDTO.BankAccountDTO accountDto : userInfo.getRes_list()) {
	                
	            	Optional<BankAccount> existingAccount = bankAccountRepository
	                        .findByUserAndFintechUseNum(user, accountDto.getFintech_use_num());
	                // 중복 체크
	            	// [핵심 수정] 
	                // 핀테크이용번호로 우리 DB에 이미 있는지 확인 (활성/비활성 상관없이)
	            	if (existingAccount.isPresent()) {
	                    // [핵심] 이미 DB에 존재한다면?
	                    // 그 계좌가 현재 'N'(false) 상태라면 아무것도 하지 않고 다음 계좌로 넘어감 (절대 업데이트 안 함)
	                    if ("N".equals(existingAccount.get().getIsActive())) {
	                        System.out.println("⚠️ 이미 사용자가 삭제한 계좌입니다. 상태 유지: " + accountDto.getBank_name());
	                        continue; 
	                    }
	                    
	               
	                    continue;
	                }

	                // 아예 처음 등록하는 계좌인 경우에만 아래 로직 실행
	                BankAccount account = new BankAccount();
	                account.setUser(user);
	                
	                account.setAccessToken(tokenDto.getAccess_token());
	                account.setRefreshToken(tokenDto.getRefresh_token());
	                account.setUserSeqNo(tokenDto.getUser_seq_no());

	                account.setBankName(accountDto.getBank_name());              
	                account.setFintechUseNum(accountDto.getFintech_use_num());   
	                account.setAccountNumMasked(accountDto.getAccount_num_masked()); 
	                account.setAccountHolderName(accountDto.getAccount_holder_name()); 
	                account.setBankCodeStd(accountDto.getBank_code_std());       
	                
	                account.setIsActive("Y"); // 처음 등록이니까 활성화!
	                account.setMain(true); 

	                bankAccountRepository.save(account);
	                System.out.println("✅ 신규 계좌 등록 완료: " + account.getBankName());
	            }
	        }
	        
	
	      
	    }
	
	// 지갑 존재 여부 확인 (Controller에서 사용)
    public boolean hasWallet(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return payWalletRepository.findByUser(user).isPresent();
    }
	
    // 3. 지갑 생성 (비밀번호 설정 포함)
    @Transactional
    public void createWallet(Long userId, String payPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 이미 지갑이 있다면 생성하지 않고 리턴 (중복 방지)
        if (payWalletRepository.findByUser(user).isPresent()) {
            return;
        }

        PayWallet newWallet = new PayWallet();
        newWallet.setUser(user);
        newWallet.setBalance(BigDecimal.ZERO);
        newWallet.setStatus(WalletStatus.ACTIVE);
        
        // ★ 여기서 사용자가 입력한 비밀번호를 저장합니다.
        // (참고: 실제 서비스에서는 BCryptPasswordEncoder 등으로 암호화해서 넣어야 안전합니다)
        newWallet.setPayPassword(payPassword); 

        payWalletRepository.save(newWallet);
    }
	
	// 마이페이지에서 지갑 정보 가져오기
	public WalletViewDTO getMyWalletPage(Long userId) {
        
        // 1. DB 조회
        WalletViewDTO walletInfo = paymentMapper.selectWalletWithAccount(userId);

        // 2. 예외 처리: 만약 지갑조차 없다면? (회원가입 시 지갑 생성 안 된 경우)
        if (walletInfo == null) {
            walletInfo = new WalletViewDTO();
            walletInfo.setBalance(0L); // 잔액 0원 처리
        }

        // 3. 계좌번호 마스킹 처리 (보안)
        // 계좌번호가 존재할 때만 처리 (예: 123456789 -> 123*******)
        if (walletInfo.getAccountNum() != null && walletInfo.getAccountNum().length() > 6) {
            String original = walletInfo.getAccountNum();
            String prefix = original.substring(0, 3); // 앞 3자리만 보여줌
            
            // 나머지는 *로 변경
            walletInfo.setAccountNumMasked(prefix + "*******");
        } else {
            // 계좌번호가 너무 짧거나 없으면 그대로 둠
            walletInfo.setAccountNumMasked(walletInfo.getAccountNum());
        }

        return walletInfo;
    }
	
	// --------------- [계좌]
	
	
	// 계좌 목록 가져오기
	public List<WalletViewDTO> getAccountList(Long userId) {
	    return paymentMapper.selectAccountList(userId);
	}
	
	// 연동 계좌 삭제하기
	@Transactional
	public void deleteAccount(Long userId, Long accountId) {
		// 1. 유저 및 계좌 존재 확인
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
		
	    //  DB에서 계좌 정보 조회 (fintech_use_num 등 API에 필요한 정보 추출)
	    BankAccount accountToDelete = bankAccountRepository.findByAccountIdAndUser(accountId, user)
	            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계좌입니다."));

	    // 2. 금융결제원 API 호출 (연동 해지 요청)
	    // API 명세: DELETE /v2.0/account/cancel
	 // 실제 API 통신 시 account.getFintechUseNum()과 user.getUserSeqNo() 사용
	    boolean isApiSuccess = openBankingService.requestAccountCancel(
	    		accountToDelete.getAccessToken(), 
	    		accountToDelete.getUserSeqNo(), 
	    		accountToDelete.getFintechUseNum());
	    System.out.println(isApiSuccess);
	   
	  // 3. [핵심] DB 삭제 대신 상태값 변경
	    accountToDelete.setIsActive("N");
	   // 2. 삭제 전, 이 계좌가 주계좌였는지 확인
	    boolean wasMain = accountToDelete.isMain();

	        
	    
	    
	    // [조건문] 주계좌를 삭제한 경우 최근 등록된 계좌를 자동으로 주계좌로 승격
	    if (wasMain) {
	        bankAccountRepository.findFirstByUserAndAccountIdNotOrderByAccountIdDesc(user, accountId)
	            .ifPresent(newMainAccount -> {
	                newMainAccount.setMain(wasMain); // 필드명이 isMain일 경우
	                // bankAccountRepository.save(newMainAccount); // @Transactional이므로 자동 감지되지만 명시해도 좋음
	            });
	    }
	   
	}

	// 주계좌 변경하기 
	@Transactional 
	public void changeMainAccount(Long userId, Long newMainAccountId) {
	    // 1) 해당 유저의 모든 계좌를 'N'으로 초기화
	    paymentMapper.resetMainAccount(userId);
	    
	    // 2) 선택한 계좌만 'Y'로 설정
	    paymentMapper.setMainAccount(newMainAccountId, userId);
	}
	
	
	
	
	
	
	// --------------- re:pay 충전하기
	public void chargePoint(Long userId, Long amount) {
	    // 0원 이하 충전 방지 로직 (선택사항)
	    if (amount <= 0) {
	        throw new IllegalArgumentException("충전 금액은 0원보다 커야 합니다.");
	    }
	    
	    // 2. 내 지갑 정보 조회 (wallet_id와 현재 잔액을 알기 위해 필수!)
	    WalletViewDTO wallet = paymentMapper.selectWalletInfo(userId);
	    if (wallet == null) {
	        throw new RuntimeException("지갑 정보를 찾을 수 없습니다.");
	    }
	    // 3. 잔액 계산 (현재 잔액 + 충전 금액)
	    Long newBalance = wallet.getBalance() + amount;
	    
	    //4.  지갑 잔액 업데이트
	    paymentMapper.chargeBalance(userId, newBalance);
	    
	    
	    // 거래 내역 객체 생성
	    WalletTransactionDTO history = new WalletTransactionDTO();
	    history.setWalletId(wallet.getWalletId());      // ★ 지갑 ID (필수)
	    history.setPaymentType(PayTransactionType.PAYMENT.CHARGE);
	    history.setAmount(amount);                      // 거래 금액
	    history.setBalanceSnapshot(newBalance);         // ★ 충전 후 잔액 (스냅샷)
	    history.setStatus(TradeStatus.SUCCESS);
	    history.setRelatedId(null);                     // 연관 ID (충전은 보통 없음)
	    
	   // 6. ★ 결제 기록 DB에 저장 
	    paymentMapper.insertTransaction(history);
	    
	}
	
	// ------ repay 출금하기
	// repay 돈 -------> 주계좌
	@Transactional
	public void processWithdraw(Long userId, Long amount) {
	    // 1. 사용자 조회
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	    
	    WalletViewDTO wallet = paymentMapper.selectWalletInfo(userId);
	

	    
	        if (wallet.getBalance() < amount) {
	            throw new RuntimeException("잔액이 부족합니다.");
	        }
	        
	        Long newBalance = wallet.getBalance() - amount;
	        wallet.setBalance(newBalance);
	        
	        //4.  지갑 잔액 업데이트
		    paymentMapper.chargeBalance(userId, newBalance);
	        
	        // 4. 출금 이력 저장 (Transaction 테이블이 있다면)
	        WalletTransactionDTO history = new WalletTransactionDTO();
	        history.setWalletId(wallet.getWalletId());
	        history.setPaymentType(PayTransactionType.PAYMENT.WITHDRAW);
	        history.setAmount(amount);                      // 거래 금액
		    history.setBalanceSnapshot(newBalance);         // ★ 충전 후 잔액 (스냅샷)
		    history.setStatus(TradeStatus.SUCCESS);
		    history.setRelatedId(null);                     // 연관 ID (없음)
		    paymentMapper.insertTransaction(history);

	        System.out.println("✅ 출금 프로세스 완료: " + amount + "원");
	    }
	
	// ------ repay 결제하기 
	// repay 상품구매 -> re:pay에서 차감
	@Transactional
	public void processPayment(Long userId, Long amount, Long productId) {
	    // 1. 사용자 조회
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	    
	    WalletViewDTO wallet = paymentMapper.selectWalletInfo(userId);
	

	    
	        if (wallet.getBalance() < amount) {
	            throw new RuntimeException("잔액이 부족합니다.");
	        }
	        
	        Long newBalance = wallet.getBalance() - amount;
	        wallet.setBalance(newBalance);
	        
	        //4.  지갑 잔액 업데이트
		    paymentMapper.chargeBalance(userId, newBalance);
	        
	        // 4. 출금 이력 저장 (Transaction 테이블이 있다면)
	        WalletTransactionDTO history = new WalletTransactionDTO();
	        history.setWalletId(wallet.getWalletId());
	        history.setPaymentType(PayTransactionType.PAYMENT.PAYMENT);
	        history.setAmount(amount);                      // 거래 금액
		    history.setBalanceSnapshot(newBalance);         //  잔액 (스냅샷)
		    history.setStatus(TradeStatus.SUCCESS);
		    history.setRelatedId(productId);                     // 연관 ID (없음)
		    paymentMapper.insertTransaction(history);

	        System.out.println("✅ 결제 re:pay 프로세스 완료: " + amount + "원");
	    }
	
	
	
	// 사용자의 모든 거래 내역을 최신순으로 가져옴
	public List<WalletTransactionDTO> getHistoryList(Long userId) {
	    
	    return paymentMapper.selectHistoryList(userId);
	}
}
