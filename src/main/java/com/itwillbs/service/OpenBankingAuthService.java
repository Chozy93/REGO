package com.itwillbs.service;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.config.FintechProperties;
import com.itwillbs.dto.PayOAuthTokenResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenBankingAuthService {
    private final FintechProperties fintechProperties;
    private final RestTemplate restTemplate = new RestTemplate();
	

    // -------------- token 발급 요청 함수
    public PayOAuthTokenResponseDTO requestToken(String code) {

        String url = fintechProperties.getBaseUrl() + "/oauth/2.0/token";

     // 1. Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. Parameter 설정 (body)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code); // 받아온 인증 코드
        params.add("client_id", fintechProperties.getClientId());
        params.add("client_secret", fintechProperties.getClientSecret());
        params.add("redirect_uri", fintechProperties.getRedirectUri());
        params.add("grant_type", "authorization_code"); // 고정값

        // 3. 요청 보내기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        // 4. 응답 받기
        ResponseEntity<PayOAuthTokenResponseDTO> response = restTemplate.postForEntity(
                url, 
                request, 
                PayOAuthTokenResponseDTO.class
        );

        return response.getBody();
    }
    
    
    
    
    // --------- 계좌 삭제 요청
    public boolean requestAccountCancel(String accessToken, String userSeqNo, String fintechUseNum) {
    	// 1. URL 경로 안전하게 조립
        String baseUrl = fintechProperties.getBaseUrl().trim();
        
        // baseUrl에 /v2.0이 포함되어 있지 않다면 추가
        if (!baseUrl.contains("/v2.0")) {
            // 끝에 슬래시가 있으면 제거하고 /v2.0 추가
            baseUrl = baseUrl.replaceAll("/$", "") + "/v2.0";
        }
        
        // 최종 URL 조립 (/v2.0/account/cancel)
        String url = baseUrl.replaceAll("/$", "") + "/account/cancel";
        
        System.out.println("🚩 실제 호출 URL: " + url);
        
     // 2. [핵심] ClientId에서 앞 10자리를 추출하여 bank_tran_id 조립
        String fullClientId = fintechProperties.getClientId().trim();
        
        // 만약 clientId가 10자보다 짧을 경우를 대비해 0으로 패딩, 길면 자름
        String myInstitutionCode = (fullClientId.length() >= 10) 
                                   ? fullClientId.substring(0, 10) 
                                   : String.format("%-10s", fullClientId).replace(' ', '0');

        String bankTranId = myInstitutionCode + "U" + generateRandom9Digits();

        System.out.println("🚩 사용 중인 Full ClientId: " + fullClientId);
        System.out.println("🚩 추출된 이용기관코드(앞10자): " + myInstitutionCode);
        System.out.println("🚩 최종 bank_tran_id (20자): " + bankTranId);

        // 1. 헤더 설정 (Access Token 포함)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        // 2. 바디 설정
        Map<String, String> body = new HashMap<>();
        body.put("user_seq_no", userSeqNo);
        body.put("fintech_use_num", fintechUseNum);
        body.put("scope", "inquiry"); // 계좌 연동 시 '조회' 권한을 사용했다면 inquiry, '이체'라면 transfer
        body.put("bank_tran_id", bankTranId);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            // 3. API 호출
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

           // [중요] 금결원에서 보내준 실제 응답 전체를 출력합니다.
            System.out.println("=== 금융결제원 API 응답 상세 ===");
            System.out.println("HTTP 상태 코드: " + response.getStatusCode());
            System.out.println("응답 바디: " + response.getBody());
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String rspCode = (String) response.getBody().get("rsp_code");
                // 응답 코드가 'A0000'(성공)인지 확인
                return "A0000".equals(rspCode);
            }
        } catch (Exception e) {
            // 로그 기록: 로깅 프레임워크 사용 권장 (e.g., SLF4J)
            System.err.println("금융결제원 API 호출 중 오류 발생: " + e.getMessage());
        }

        return false;
    }
    
 // 9자리 랜덤 숫자 생성 도우미
    private String generateRandom9Digits() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
}
