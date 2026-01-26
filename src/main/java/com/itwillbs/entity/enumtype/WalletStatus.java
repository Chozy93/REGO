package com.itwillbs.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WalletStatus {
	
	ACTIVE("정상"),     
    LOCKED("잠김"),     
    SUSPENDED("정지");
	
// -------- "정상" 같은 한글 값 저장
    private final String label;

}
