package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.PayWallet;
import com.itwillbs.entity.User;

public interface PayWalletRepository extends JpaRepository<PayWallet, Long>{
	// 특정 유저의 지갑이 이미 있는지 확인할 때 사용
    Optional<PayWallet> findByUser(User user);
}
