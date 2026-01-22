package com.itwillbs.entity.enumtype;

public enum EscrowStatus {
	PENDING("결제 대기"),    // 결제 대기
    HELD("결제 완료, 돈 보관 중"),       // 결제 완료 (돈 보관 중)
    RELEASED("거래 확정, 돈 지급됨"),   // 거래 확정 (돈 지급됨)
    CANCELLED("거래 취소");   // 거래 취소
	
	
	  private final String label;

	  EscrowStatus(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }
}
