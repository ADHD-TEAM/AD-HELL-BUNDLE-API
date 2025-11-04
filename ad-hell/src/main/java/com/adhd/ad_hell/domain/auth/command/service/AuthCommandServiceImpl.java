package com.adhd.ad_hell.domain.auth.command.service;

import com.adhd.ad_hell.EmailVerificationCode;
import com.adhd.ad_hell.common.dto.CustomUserDetails;
import com.adhd.ad_hell.domain.auth.command.dto.request.ExistVerificationCodeRequest;
import com.adhd.ad_hell.domain.auth.command.dto.request.LoginRequest;
import com.adhd.ad_hell.domain.auth.command.dto.request.SendEmailVerifyUserRequest;
import com.adhd.ad_hell.domain.auth.command.dto.response.ExistVerificationCodeResponse;
import com.adhd.ad_hell.domain.auth.command.dto.response.TokenResponse;
import com.adhd.ad_hell.domain.auth.command.entity.RefreshToken;
import com.adhd.ad_hell.domain.auth.command.repository.RefreshTokenRepository;
import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.command.repository.UserCommandRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import com.adhd.ad_hell.jwt.JwtProperties;
import com.adhd.ad_hell.jwt.JwtTokenProvider;
import com.adhd.ad_hell.mail.MailService;
import com.adhd.ad_hell.mail.MailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final UserCommandRepository userCommandRepository;

    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final MailService mailService;
    private final AuthRedisService authRedisService;



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
    public void logout(CustomUserDetails request) {
        // refresh token 제거
        refreshTokenRepository.deleteById(request.getLoginId());
    }

    @Override
    @Transactional
    public void sendEmail(SendEmailVerifyUserRequest request) {
        log.info("[AuthCommandServiceImpl/sendEmail] 본인인증 - 이메일로 인증번호 보내기 |");
        String toEmail = request.getEmail();
        String receiverName = request.getLoginid();
        MailType mailType = MailType.VERIFICATION;

        // 인증번호 생성
        String code = EmailVerificationCode.getCode();
        log.info("[AuthCommandServiceImpl/sendEmail] 본인인증 인증번호 생성 , code {} |", code);

        // 레디스 저장
        authRedisService.saveValidityCode(toEmail, code);
        log.info("[AuthCommandServiceImpl/sendEmail] 레디스 저장 , toEmail, code {} , {} |",toEmail, code);

        // 이메일 발송
        try {
            mailService.sendMail(toEmail, receiverName, mailType, code);
            log.info("[AuthCommandServiceImpl/sendEmail] 이메일 발송 성공 ");
        } catch (Exception e) {
            throw new RuntimeException("메일 전송 실패");
        }

    }

    @Override
    public ExistVerificationCodeResponse existVerificationCode(
            ExistVerificationCodeRequest request) {
        log.info("[AuthCommandServiceImpl/sendEmail] 인증번호 있는지 확인");
        // 레디스에서 email로 key값과 value 값이 있는
         Boolean exist = authRedisService.existVerificationCode(
                 request.getEmail(), request.getVerificationCode());
        log.info("[AuthCommandServiceImpl/sendEmail] 인증번호 exist={}", exist);
        return ExistVerificationCodeResponse.builder()
                .exist(exist).build();
    }

}
