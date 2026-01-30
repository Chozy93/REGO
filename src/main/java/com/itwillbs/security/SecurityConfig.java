package com.itwillbs.security;

import com.itwillbs.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    // 순환 참조 방지를 위해 @Lazy로 주입받기
    @Lazy
    private final CustomOAuth2UserService customOAuth2UserService;

    private final  CustomSuccessHandler customSuccessHandler;
    
    private final CustomAuthenticationFailureHandler customFailureHandler;
    
    private final CustomAjaxLoginSuccessHandler customAjaxLoginSuccessHandler;
    
    private final LoginRequiredEntryPoint loginRequiredEntryPoint;

    /* =========================
       AuthenticationManager
    ========================= */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /* =========================
       Security Filter Chain
    ======================== */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

       
		http
            /* ---------- CSRF ---------- */
            .csrf(csrf -> csrf.disable())
            
            /* ---------- HTTP Basic 비활성화 ---------- */
            .httpBasic(basic -> basic.disable())

            .authorizeHttpRequests(auth -> auth
            		 /* =========================
                    로그인 필요
                 ========================= */
                 .requestMatchers(
                     "/chat/**",
                     "/mypage/**",
                     "/review/**",
                     "/customer/inquiries/**",
                     "/report/**",
                     "/seller/product/**",
                     "/order/**",
                     "/direct/**",
                     "/seller/**",
                     "/myrepay/**",
                     "/pay/**"
                     
                 ).authenticated()

                    /* =========================
                       공개 페이지
                    ========================= */
                    .requestMatchers(
                        "/",
                        "/products/**",
                        "/customer/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/callback/**",
                        "/login-required",
                        "/auth/**",
                        "/complete-info",
                        "/login/**",
                        "/signup/**",
                        "/api/**",
                        "/seller/profile/**",
                        "/product/**"
                    ).permitAll()

                   
                 
                    /* =========================
                       관리자 전용
                    ========================= */
                    .requestMatchers(
                        "/admin/**"
                    ).hasRole("ADMIN")

                    /* =========================
                       그 외는 차단
                    ========================= */
                    .anyRequest().denyAll()
                )
            .exceptionHandling(ex -> ex
                	    .authenticationEntryPoint(loginRequiredEntryPoint)
                		)


            /* ---------- 일반 폼 로그인 (SSR + 모달) ---------- */
            .formLogin(login -> login
            	    .loginProcessingUrl("/login")
            	    .usernameParameter("email")
            	    .passwordParameter("password")
            	    // ✅ 성공/실패 모두 핸들러로
            	    .successHandler(customAjaxLoginSuccessHandler)   // 👈 새로 만들 것
            	    .failureHandler(customFailureHandler)
            	)

            /* ---------- 소셜 로그인 ---------- */
            .oauth2Login(oauth2 -> oauth2
            	    .userInfoEndpoint(userInfo -> userInfo
            	        .userService(customOAuth2UserService)
            	    )
            	    .successHandler(customSuccessHandler)
            	)

            /* ---------- 로그아웃 ---------- */
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}