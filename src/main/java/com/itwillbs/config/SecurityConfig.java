package com.itwillbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.itwillbs.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRF 끄기 (WebSocket + 테스트 단계)
            .csrf(csrf -> csrf.disable())

            // 권한 전부 열기
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/chat/**",
                    "/ws-chat/**",
                    "/app/**",
                    "/topic/**",
                    "/css/**",
                    "/js/**",
                    "/img/**"
                ).permitAll()
                .anyRequest().permitAll()
            )

            // 로그인 폼 안 씀
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") 
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                        )
             );
        return http.build();
    }
}
