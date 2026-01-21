package com.itwillbs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

// 금융결제원 api key 설정 클래스

@Component
@ConfigurationProperties(prefix = "fintech")
@Getter
@Setter
public class FintechProperties {
    private String clientId;
    private String clientSecret;
    private String baseUrl;
    private String redirectUri;
    
   
	
}
