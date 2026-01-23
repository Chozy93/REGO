package com.itwillbs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

@Configuration
public class IamportConfig {
	@Value("${iamport.imp_key}")
    private String apiKey;

    @Value("${iamport.imp_secret}")
    private String apiSecret;

    @Bean
    public IamportClient iamportClient() {
        // 이제 스프링이 이 Bean을 관리하므로 컨트롤러에서 주입받을 수 있습니다.
        return new IamportClient(apiKey, apiSecret);
    }
}
