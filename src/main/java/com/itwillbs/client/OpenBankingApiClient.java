package com.itwillbs.client;

import com.itwillbs.dto.OpenBankingUserInfoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
// 금융결제원 api에 요청 보내고 응답 받는 코드
public class OpenBankingApiClient {

    // Spring이 제공하는 HTTP 클라이언트 (없으면 new RestTemplate() 해도 됨)
    private final RestTemplate restTemplate = new RestTemplate();

    // 오픈뱅킹 테스트베드 주소
    private static final String OPEN_BANKING_URL = "https://testapi.openbanking.or.kr";

    /**
     * [2.0] 사용자/계좌 정보 조회
     * 토큰과 사용자일련번호를 주면 -> 계좌 목록(은행명 포함)이 담긴 DTO를 리턴
     */
    public OpenBankingUserInfoResponseDTO requestUserInfo(String accessToken, String userSeqNo) {
        
        String url = OPEN_BANKING_URL + "/v2.0/user/me";

        // 1. 헤더 설정 (Authorization: Bearer <token>)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // 2. 쿼리 파라미터 설정 (?user_seq_no=...)
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("user_seq_no", userSeqNo);

        // 3. 요청 객체 생성
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            log.info("오픈뱅킹 조회 요청: {}", builder.toUriString());
            
            // 4. 전송 및 응답 받기
            ResponseEntity<OpenBankingUserInfoResponseDTO> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    OpenBankingUserInfoResponseDTO.class
            );

            log.info("오픈뱅킹 응답 완료: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("오픈뱅킹 API 호출 중 에러 발생", e);
            throw new RuntimeException("계좌 정보를 불러오는 데 실패했습니다.");
        }
    }
}
