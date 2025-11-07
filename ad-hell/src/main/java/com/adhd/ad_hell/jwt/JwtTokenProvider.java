package com.adhd.ad_hell.jwt;

import com.adhd.ad_hell.domain.user.command.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties; // JWT 관련 설정 (secretKey, expiration 등)
    private SecretKey secretKey;

    @PostConstruct
    public void init() {    // 시크릿 키 초기화 (Notification 쪽과 동일한 방식)
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    // ===================== 토큰 생성 =====================

    /**
     * access token 생성
     */
    public String createAccessToken(String loginId, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(loginId)
                .claim("role", Role.valueOf(role.name()))
                // 필요하면 여기서 .claim("userId", userId) 같은 것도 추가 가능
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * refresh token 생성
     */
    public String createRefreshToken(String loginId, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshExpiration());

        return Jwts.builder()
                .subject(loginId)
                .claim("role", Role.valueOf(role.name()))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    // ===================== 토큰 검증 =====================

    /**
     * 기존 코어에서 쓰던 메서드 (오타 포함 유지)
     * - 유효하지 않으면 BadCredentialsException 던짐
     */
    public boolean vaildateToken(String token) {
        try {
            getClaims(token); // 파싱 + 서명 + 만료 검증
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new BadCredentialsException("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            throw new BadCredentialsException("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("JWT Token claims empty", e);
        }
    }

    /**
     * NotificationJwtTokenProvider 의 boolean validateToken 역할
     * - 예외를 던지지 않고 false 반환
     */
    public boolean validateToken(String token) {
        try {
            return vaildateToken(token);
        } catch (BadCredentialsException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // ===================== 클레임 / 정보 추출 =====================

    /**
     * 토큰으로 subject(loginId) 가져오기
     * (기존 getUserIdFromJWT 유지)
     */
    public String getUserIdFromJWT(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * NotificationJwtTokenProvider.getLoginId 대체
     */
    public String getLoginId(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * NotificationJwtTokenProvider.getUserId 대체
     * - 토큰에 userId 클레임을 넣었다는 가정
     * - 없으면 null
     */
    public Long getUserId(String token) {
        Object claim = getClaims(token).get("userId");
        if (claim instanceof Number num) {
            return num.longValue();
        }
        if (claim instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * role 클레임 꺼내기
     */
    public String getRole(String token) {
        Object claim = getClaims(token).get("role");
        return claim != null ? claim.toString() : null;
    }

    /**
     * 토큰 남은 유효시간
     */
    public long getRemainingTime(String accessToken) {
        Claims claims = getClaims(accessToken);
        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        return expiration.getTime() - now;
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

    // ===================== 내부 공통 유틸 =====================

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)  // NotificationJwtTokenProvider 와 동일한 방식
                .getPayload();             // claims
    }
}
