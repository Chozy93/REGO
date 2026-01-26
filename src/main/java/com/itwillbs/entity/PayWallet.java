package com.itwillbs.entity;

import java.math.BigDecimal;
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
    private BigDecimal balance = BigDecimal.ZERO;
    
    
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
    
    
    /**
     * 출금 로직
     */
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("출금 금액은 0보다 커야 합니다.");
        }
        
        // 잔액 검증: (잔액 - 출금액) < 0 인지 확인
        if (this.balance.compareTo(amount) < 0) {
            throw new RuntimeException("re:pay 잔액이 부족합니다.");
        }
        
        // 핵심: BigDecimal은 불변 객체이므로 연산 결과를 다시 할당해야 함
        this.balance = this.balance.subtract(amount);
    }
    
    
    /**
     * 입금 로직
     */
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("입금 금액은 0보다 커야 합니다.");
        }
        
        this.balance = this.balance.add(amount);
    }
	
}
