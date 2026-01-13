package com.itwillbs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /* =========================
       비밀번호 암호화
    ========================= */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
    ========================= */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            /* ---------- CSRF ---------- */
            .csrf(csrf -> csrf.disable())
            // SSR이지만, 현재는 Ajax POST 위주 구조 → 추후 필요 시 재검토

//            /* ---------- 요청 권한 ---------- */
//            .authorizeHttpRequests(auth -> auth
//                // 정적 리소스
//                .requestMatchers(
//                    "/css/**",
//                    "/js/**",
//                    "/images/**"
//                ).permitAll()
//
//                // 인증 없이 접근 가능
//                .requestMatchers(
//                    "/",
//                    "/login",
//                    "/signup"
//                ).permitAll()
//
//                // 관리자
//                .requestMatchers("/admin/**")
//                .hasRole("ADMIN")
//
//                // 로그인 필요
//                .requestMatchers(
//                    "/mypage/**",
//                    "/chat/**"
//                ).authenticated()
//
//                // 그 외
//                .anyRequest().permitAll()
//            )
            
            /* ---------- 요청 권한 ---------- */
            .authorizeHttpRequests(auth -> auth
                // 🔧 개발 단계: 모든 요청 허용
                .anyRequest().permitAll()
            )

            /* ---------- 로그인 ---------- */
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
            )

            /* ---------- 로그아웃 ---------- */
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
