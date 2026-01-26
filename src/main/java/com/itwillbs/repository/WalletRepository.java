package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.PayWallet;

import jakarta.persistence.LockModeType;

@Repository
public interface WalletRepository extends JpaRepository<PayWallet, Long> {
	
	// 유저 ID로 지갑 조회
    Optional<PayWallet> findByUser_UserId(Long userId);

    /**
     * 결제 처리 시 잔액 수정을 위해 데이터베이스 수준에서 락을 겁니다.
     * (동시 결제 시 잔액 꼬임 방지)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM PayWallet w WHERE w.user.userId = :userId")
    Optional<PayWallet> findByUserIdWithLock(@Param("userId") Long userId);

}
