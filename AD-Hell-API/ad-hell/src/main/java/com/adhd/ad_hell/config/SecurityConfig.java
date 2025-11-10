package com.adhd.ad_hell.config;

import com.adhd.ad_hell.common.ApiEndpoint;
import com.adhd.ad_hell.domain.auth.command.service.CustomUserDetailsService;
import com.adhd.ad_hell.domain.user.command.entity.Role;
import com.adhd.ad_hell.jwt.JwtAuthentiationFilter;
import com.adhd.ad_hell.jwt.JwtTokenProvider;
import com.adhd.ad_hell.jwt.RestAccessDeniedHandler;
import com.adhd.ad_hell.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;                    // 토큰 생성/검증
    private final CustomUserDetailsService userDetailsService;          // 사용자 정보 로드
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint; // 인증 실패 핸들러
    private final RestAccessDeniedHandler restAccessDeniedHandler;           // 인가 실패 핸들러

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRF 비활성화 (JWT 사용)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 세션을 사용하지 않는 Stateless 설정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. URL / Method 별 인가 규칙
                .authorizeHttpRequests(auth -> {

                    /* Swagger 문서 공개 */
                    auth.requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                    ).permitAll();

                    /* 정적 리소스 & SSE 테스트 페이지 */
                    auth.requestMatchers(
                            "/",              // 루트
                            "/index.html",
                            "/sse-test.html", // SSE 테스트용 HTML
                            "/static/**",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/favicon.ico"
                    ).permitAll();

                    /* 🔓 개발용: SSE 스트림 엔드포인트 토큰 없이 허용 */
                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/api/users/*/notifications/stream"
                    ).permitAll();

                    /* 내부 시스템 간 호출은 열어둠 */
                    auth.requestMatchers("/internal/notifications/**").permitAll();

                    /* 사용자 알림 관련 API 는 인증 필수 */
                    auth.requestMatchers("/api/users/*/notifications/**").authenticated();

                    /* 관리자용 API */
                    auth.requestMatchers("/api/admin/**").hasRole("ADMIN");

                    /* 공통 ApiEndpoint 기반 인가 처리 */
                    for (ApiEndpoint endpoint : ApiEndpoint.values()) {
                        if (endpoint.getRole() == null) {
                            // 예: 회원가입, 로그인 등 공개 엔드포인트
                            auth.requestMatchers(endpoint.getEndpointStatus(), endpoint.getPath())
                                    .permitAll();
                        } else if (endpoint.getRole() == Role.USER) {
                            // USER 권한(또는 ADMIN) 필요
                            auth.requestMatchers(endpoint.getEndpointStatus(), endpoint.getPath())
                                    .hasAnyRole(Role.USER.name(), Role.ADMIN.name());
                        } else if (endpoint.getRole() == Role.ADMIN) {
                            // ADMIN 전용
                            auth.requestMatchers(endpoint.getEndpointStatus(), endpoint.getPath())
                                    .hasRole(Role.ADMIN.name());
                        }
                    }

                    /* 위에서 명시하지 않은 모든 요청은 인증 필요 */
                    auth.anyRequest().authenticated();
                })

                // 4. JWT 인증 필터 추가
                .addFilterBefore(jwtAuthentiationFilter(), UsernamePasswordAuthenticationFilter.class)

                // 5. 인증 / 인가 실패 처리
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                                .accessDeniedHandler(restAccessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public JwtAuthentiationFilter jwtAuthentiationFilter() {
        return new JwtAuthentiationFilter(jwtTokenProvider, userDetailsService);
    }
}
