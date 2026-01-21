package com.itwillbs.domain;

public class BankAccountVO {
	
	private Long accountId;      // PK
    private Long userId;         // FK (사용자 PK)
    
    private String bankName;     // 은행명 (추후 조회 API로 채움)
    private String accountNum;   // 계좌번호 (추후 조회 API로 채움)
    private String fintechUseNum;// 핀테크이용번호 (출금 때 필수)
    
    private String accessToken;  // 접근 토큰
    private String refreshToken; // 갱신 토큰
    private String userSeqNo;    // 사용자 일련번호

}
