package com.itwillbs.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import com.itwillbs.entity.enumtype.WalletStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "wallets")
public class PayWallet {
	
	//---- pk 	-----
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;
    
	// 1:1 관계 (User 한 명당 지갑 하나)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    
    // 잔액 (기본값 0원)
    @Column(nullable = false)
    @ColumnDefault("0") // DB에 들어갈 기본값
    private Long balance = 0L;
    
    
    // wallet 비밀번호 (페이 충전 + 결제 시 사용)
    @Column(name = "pay_password")
    private String payPassword;
    
 // wallet 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @ColumnDefault("'ACTIVE'")
    private WalletStatus status = WalletStatus.ACTIVE;
    
    // 지갑 생성 시간
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    
    // 지갑 상태 변동 시간
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
	
}
