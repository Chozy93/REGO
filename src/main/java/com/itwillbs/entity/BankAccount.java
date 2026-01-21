package com.itwillbs.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor // JPA 필수 (빈 깡통 객체 생성용)
@DynamicInsert // insert 시 null인 필드 제외 -> DB Default값 적용
@Table(name = "bank_accounts")
public class BankAccount {
	
	// --------- pk ------------
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;
	
	
	// 한 명의 유저가 여러 계좌를 가질 수 있음 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
   // --- 오픈뱅킹 필수 데이터 (토큰 & 식별번호) --------------------------------
    // -------------------------- 
    
    @Column(name = "access_token", length = 1000)
    private String accessToken;
    
    
    @Column(name = "refresh_token", length = 1000)
    private String refreshToken;
    
    // 사용자 일련번호
    @Column(name = "user_seq_no", nullable = false) 
    private String userSeqNo;
	
 // 핀테크 이용번호
    @Column(name = "fintech_use_num") 
    private String fintechUseNum;

    // ---------------------------------------------- //
    //  -------------------------
    
    
    
    
 // --- 화면 표시 데이터 ---
 // 은행 이름 -> 예: KB국민은행 
    @Column(name = "bank_name", length = 50)
    private String bankName; 
    
    
    @Column(name = "account_num_masked", length = 50)
    private String accountNumMasked; // 예: 123-***-7890
    
    // 대표 계좌 여부 (Y/N)
    @Column(name = "is_primary", length = 1)
    @ColumnDefault("'N'")
    private String isPrimary = "N";
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
