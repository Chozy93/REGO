package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.BankAccount;
import com.itwillbs.entity.User;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>{
	// "이 유저"가 "이 일련번호(userSeqNo)"를 가진 계좌를 이미 등록했는지 확인
    Optional<BankAccount> findByUserAndUserSeqNo(User user, String userSeqNo);
}
