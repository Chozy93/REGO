package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.dto.PayOAuthTokenResponseDTO;
import com.itwillbs.dto.WalletViewDTO;
import com.itwillbs.entity.BankAccount;
import com.itwillbs.entity.PayWallet;
import com.itwillbs.entity.User;
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
	
    
	 // 계좌 연동 및 지갑 생성 (Command -> JPA 사용) [DB에 저장하는 과정]
	@Transactional
	public void registerAccount(Long userId, PayOAuthTokenResponseDTO tokenDto) {
	        
	        // 1. 사용자 조회
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	
	        // 2. 계좌 정보 저장 (Setter 방식)
	        // 이미 연동된 계좌가 있는지 확인 (중복 방지)
	        BankAccount account = bankAccountRepository.findByUserAndUserSeqNo(user, tokenDto.getUser_seq_no())
	                .orElse(new BankAccount()); // 없으면 새로 생성
	        
	        account.setUser(user);
	        account.setAccessToken(tokenDto.getAccess_token());
	        account.setRefreshToken(tokenDto.getRefresh_token());
	        account.setUserSeqNo(tokenDto.getUser_seq_no());
	        // 핀테크 이용번호 등은 나중에 조회 API 호출 시 업데이트 가능
	        
	        bankAccountRepository.save(account);
	
	        // 3. 지갑 생성 (아직 지갑이 없는 경우에만 생성)
	        if (payWalletRepository.findByUser(user).isEmpty()) {
	            PayWallet newWallet = new PayWallet();
	            newWallet.setUser(user);
	            newWallet.setBalance(0L);
	            newWallet.setStatus(WalletStatus.ACTIVE); // Enum 사용
	            
	            payWalletRepository.save(newWallet);
	        }
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
}
