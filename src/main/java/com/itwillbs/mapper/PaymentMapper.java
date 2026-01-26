package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.WalletTransactionDTO;
import com.itwillbs.dto.WalletViewDTO;

@Mapper
public interface PaymentMapper {

	// 유저 id루 지갑+계좌 정보 조회
	WalletViewDTO selectWalletWithAccount(Long userId);
	// 계좌 목록
	List<WalletViewDTO> selectAccountList(Long userId);

	// 주계좌 변경용 (두 개를 같이 씁니다)
	void resetMainAccount(@Param("userId") Long userId);
	void setMainAccount(@Param("accountId") Long accountId, @Param("userId")Long userId);
	
	// re:pay 잔액 충전
	void chargeBalance(@Param("userId") Long userId, @Param("amount") Long amount);
	
	// 내 지갑 정보 조회 (userId로 walletId와 balance를 찾음)
    WalletViewDTO selectWalletInfo(Long userId);
    
    // ★ 거래 내역 저장 (DTO 통째로 받음)
    void insertTransaction(WalletTransactionDTO history);
    
    // 거래 내역 조회
    List<WalletTransactionDTO> selectHistoryList(Long userId);
}
