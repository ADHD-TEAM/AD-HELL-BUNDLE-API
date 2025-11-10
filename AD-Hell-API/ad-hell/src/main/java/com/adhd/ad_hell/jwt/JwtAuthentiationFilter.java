package com.adhd.ad_hell.jwt;

import com.adhd.ad_hell.common.dto.CustomUserDetails;
import com.adhd.ad_hell.domain.auth.command.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthentiationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("[JwtAuthentiationFilter] JWT 인증 필터 시작: {} {}", request.getMethod(), request.getRequestURI());

        // 1) 헤더에서 토큰 추출
        String token = resolveToken(request);
        log.debug("[JwtAuthentiationFilter] 추출된 토큰: {}", token);

        try {
            // 2) 토큰이 있고, 유효하면 처리
            if (StringUtils.hasText(token) && jwtTokenProvider.vaildateToken(token)) {
                log.debug("[JwtAuthentiationFilter] 토큰 유효성 검증 통과");

                // 토큰에서 loginId / userId 등 필요한 값 꺼내기
                String loginId = jwtTokenProvider.getUserIdFromJWT(token); // 기존 메서드 그대로 사용
                log.debug("[JwtAuthentiationFilter] 토큰에서 추출한 loginId: {}", loginId);

                // DB에서 사용자 정보 조회 (NotificationJwtAuthenticationFilter에서 하던 역할 포함)
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
                log.debug("[JwtAuthentiationFilter] DB에서 조회한 사용자: {}", userDetails.getUsername());

                // Authentication 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // SecurityContext 에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("[JwtAuthentiationFilter] SecurityContext 에 인증 정보 세팅 완료");
            } else {
                log.debug("[JwtAuthentiationFilter] 토큰이 없거나, 유효하지 않음");
            }
        } catch (Exception e) {
            // 토큰 파싱 / UserDetails 조회 중 예외가 나더라도 요청 자체는 계속 흐르게 함
            log.warn("[JwtAuthentiationFilter] JWT 처리 중 예외 발생: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 "Bearer xxx" 형태로 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        log.debug("[JwtAuthentiationFilter] 헤더에서 JWT 토큰 추출 시도");
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}
