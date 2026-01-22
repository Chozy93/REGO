package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.BankAccount;
import com.itwillbs.entity.User;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>{
	// 사용자와 계좌 ID로 특정 계좌 찾기 (본인 확인용)
    Optional<BankAccount> findByAccountIdAndUser(Long accountId, User user);
    
 // 특정 유저의 계좌 목록 중, 현재 삭제된 계좌를 제외하고 가장 최근에 등록된(ID가 큰) 계좌 하나 가져오기
    Optional<BankAccount> findFirstByUserAndAccountIdNotOrderByAccountIdDesc(User user, Long excludedAccountId);
	
	// 사용자와 일련번호로 계좌 찾기
    Optional<BankAccount> findByUserAndUserSeqNo(User user, String userSeqNo);
   // 핀테크 이용번호가 이미 존재하는지 확인 (true/false 반환)
    boolean existsByFintechUseNum(String fintechUseNum);
    
    // 핀테크 번호 중복되는지 확인
    Optional<BankAccount> findByUserAndFintechUseNum(User user, String fintechUseNum);
    
    Optional<BankAccount> findByFintechUseNum(String fintechUseNum);

}
