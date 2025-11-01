package com.adhd.ad_hell.domain.auth.command.service;

import com.adhd.ad_hell.common.dto.CustomUserDetails;
import com.adhd.ad_hell.domain.auth.command.dto.request.LoginRequest;
import com.adhd.ad_hell.domain.auth.command.dto.response.TokenResponse;
import com.adhd.ad_hell.domain.auth.command.entity.RefreshToken;
import com.adhd.ad_hell.domain.auth.command.repository.RefreshTokenRepository;
import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.command.repository.UserCommandRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import com.adhd.ad_hell.jwt.JwtProperties;
import com.adhd.ad_hell.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final UserCommandRepository userCommandRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        // 아이디 검증
        log.debug("[AuthCommandServiceImpl/login]로그인 아이디 검증");
        User user = userCommandRepository.findByLoginId(request.getUserLoginId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.INVALID_USERNAME_OR_PASSWORD));

        // 비밀번호 검증
        log.debug("[AuthCommandServiceImpl/login]비밀번호 검증");
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }

        // TODO : 로그아웃 구현 후
        // db에 없거나 만료 될 경우 새로 발급하는 코드 만들기
        // RefreshToken 으로 있는지 확인
        // db에 없거나 만료 된 경우 : 새로발급
        // 만료되지 않은 경우 기존 refresh 토큰 그대로 사용
        // access token은 항상 새로 발급


        // 로그인 성공시 token 발금
        log.debug("[AuthCommandServiceImpl/login]로그인 token 발급");
        String accessToken = jwtTokenProvider.createAccessToken(user.getLoginId(),user.getRoleType());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId(),user.getRoleType());

        log.info("[AuthCommandServiceImpl/login] accessToken : {}", accessToken);
        log.info("[AuthCommandServiceImpl/login] refreshToken : {}", refreshToken);

        // 일단 DB 저장

        RefreshToken tokenEntity = RefreshToken.builder()
                .userId(user.getUserId().toString())
                .token(refreshToken)
                .expiryDate(
                        new Date(System.currentTimeMillis()
                        + jwtProperties.getRefreshExpiration() )
                )
                .build();
        log.debug("[AuthCommandServiceImpl/login]  DB 저장 ");
        refreshTokenRepository.save(tokenEntity);

        log.info("[AuthCommandServiceImpl/login]  DB 저장 성공 ");
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public void logout(CustomUserDetails customUserDetails) {
        // refresh token 제거
        refreshTokenRepository.deleteById(customUserDetails.getLoginId());
    }


}
