package com.itwillbs.dto;

import lombok.Data;

@Data
public class WalletViewDTO {
	// 지갑 정보
    private Long balance;            // 잔액 (예: 532000)
    
    // 계좌 정보 (연동 안 했으면 null)
    private String bankName;         // 은행명
    private String accountNum;     // 실제 계좌번호 (DB 원본)
    private String accountNumMasked; // 마스킹된 계좌번호

}
