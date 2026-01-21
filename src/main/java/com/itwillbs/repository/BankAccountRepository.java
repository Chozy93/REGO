package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.BankAccount;
import com.itwillbs.entity.User;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>{
	// 사용자와 일련번호로 계좌 찾기
    Optional<BankAccount> findByUserAndUserSeqNo(User user, String userSeqNo);
   // 핀테크 이용번호가 이미 존재하는지 확인 (true/false 반환)
    boolean existsByFintechUseNum(String fintechUseNum);
}
