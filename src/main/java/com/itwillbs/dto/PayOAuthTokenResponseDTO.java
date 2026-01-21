package com.itwillbs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

// 금융결제원 OAuth 요청 했을 때 response data 형식
public class PayOAuthTokenResponseDTO {
	private String access_token;   // 접근 토큰 (API 호출 열쇠)
    private String token_type;     // Bearer (고정값)
    private int expires_in;        // 유효시간 (초 단위)
    private String scope;          // 권한 범위
    private String refresh_token;  // 갱신 토큰 (Access Token 만료되면 이걸로 재발급)
    private String user_seq_no;    // 사용자 일련번호 (사용자 식별용 고유 ID)

}
