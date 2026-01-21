package com.itwillbs.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
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
    
}
