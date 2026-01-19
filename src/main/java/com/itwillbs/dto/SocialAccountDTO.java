package com.itwillbs.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SocialAccountDTO {
    private Long socialId;      // 소셜 아이디 (PK)
    private String provider;      // kakao, naver, google
    private String providerId;    // 각 플랫폼에서 준 고유 번호
    private String userId;        // USER 테이블과 연결될 아이디 
    private LocalDateTime connectedAt;
}