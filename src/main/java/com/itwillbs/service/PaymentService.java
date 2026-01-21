package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.client.OpenBankingApiClient;
import com.itwillbs.dto.OpenBankingUserInfoResponseDTO;
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
    private final OpenBankingApiClient openBankingApiClient;
	
    
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
	                
	                // 중복 체크
	                if (bankAccountRepository.existsByFintechUseNum(accountDto.getFintech_use_num())) {
	                    continue; 
	                }

	                BankAccount account = new BankAccount();
	                account.setUser(user);
	                
	                // 토큰값 저장
	                account.setAccessToken(tokenDto.getAccess_token());
	                account.setRefreshToken(tokenDto.getRefresh_token());
	                account.setUserSeqNo(tokenDto.getUser_seq_no());

	                // ★ 은행 상세 정보 저장 (DTO에서 꺼냄)
	                account.setBankName(accountDto.getBank_name());              
	                account.setFintechUseNum(accountDto.getFintech_use_num());   
	                account.setAccountNumMasked(accountDto.getAccount_num_masked()); 
	                account.setAccountHolderName(accountDto.getAccount_holder_name()); 
	                account.setBankCodeStd(accountDto.getBank_code_std());       
	                
	                account.setMain(false); 

	                bankAccountRepository.save(account);
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
        newWallet.setBalance(0L);
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
}
