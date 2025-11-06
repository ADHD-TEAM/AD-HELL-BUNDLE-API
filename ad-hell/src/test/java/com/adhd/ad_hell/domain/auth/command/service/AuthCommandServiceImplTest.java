package com.adhd.ad_hell.domain.auth.command.service;

import com.adhd.ad_hell.EmailVerificationCode;
import com.adhd.ad_hell.common.dto.LoginUserInfo;
import com.adhd.ad_hell.common.util.SecurityUtil;
import com.adhd.ad_hell.domain.auth.command.dto.request.*;
import com.adhd.ad_hell.domain.auth.command.dto.response.ExistVerificationCodeResponse;
import com.adhd.ad_hell.domain.auth.command.dto.response.FindUserInfoResponse;
import com.adhd.ad_hell.domain.auth.command.dto.response.TokenResponse;
import com.adhd.ad_hell.domain.user.command.entity.Role;
import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.command.entity.UserStatus;
import com.adhd.ad_hell.domain.user.command.repository.UserCommandRepository;
import com.adhd.ad_hell.domain.user.query.dto.UserDTO;
import com.adhd.ad_hell.domain.user.query.mapper.UserMapper;
import com.adhd.ad_hell.jwt.JwtTokenProvider;
import com.adhd.ad_hell.mail.MailService;
import com.adhd.ad_hell.mail.MailType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthCommandServiceImplTest {

    @Mock
    private MailService mailService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserCommandRepository userCommandRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthRedisService authRedisService;

    @InjectMocks
    private AuthCommandServiceImpl authCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_success() {
        // given
        LoginRequest request = LoginRequest.builder()
                .userLoginId("testUser")
                .password("password123")
                .build();

        User mockUser = User.builder()
                .userId(1L)
                .loginId("testUser") // LoginRequest.userLoginId와 정확히 일치
                .password("encodedPassword") // passwordEncoder.matches에서 비교할 값
                .nickname("nickname")
                .roleType(Role.USER)
                .status(UserStatus.ACTIVATE)
                .amount(100)
                .build();

        when(userCommandRepository.findByLoginId("testUser")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        when(jwtTokenProvider.createAccessToken(anyString(), any())).thenReturn("newAccessToken");
        when(jwtTokenProvider.createRefreshToken(anyString(), any())).thenReturn("newRefreshToken");

        // when
        TokenResponse response = authCommandService.login(request);

        // then
        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());

        verify(authRedisService).saveRefreshToken(mockUser.getUserId(), "newRefreshToken");
    }

    @Test
    void tokenReissue_success() {
        // given
        TokenRequest request = TokenRequest.builder()
                .refreshToken("dummyRefreshToken")
                .build();

        LoginUserInfo mockUser = new LoginUserInfo(1L, "testUser", Role.USER);

        // SecurityUtil static mock
        try (var mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getLoginUserInfo).thenReturn(mockUser);

            when(jwtTokenProvider.vaildateToken("dummyRefreshToken")).thenReturn(true);
            when(authRedisService.existRefreshTokenByUserId(mockUser.getUserId())).thenReturn(true);
            when(jwtTokenProvider.createAccessToken(mockUser.getLoginId(), mockUser.getRole())).thenReturn("newAccessToken");
            when(jwtTokenProvider.createRefreshToken(mockUser.getLoginId(), mockUser.getRole())).thenReturn("newRefreshToken");

            // when
            TokenResponse response = authCommandService.tokenReissue(request);

            // then
            assertNotNull(response);
            assertEquals("newAccessToken", response.getAccessToken());
            assertEquals("newRefreshToken", response.getRefreshToken());

            verify(authRedisService).saveRefreshToken(mockUser.getUserId(), "newRefreshToken");
        }
    }


    @Test
    void logout_success() {
        // given
        String accessToken = "dummyAccessToken";
        LoginUserInfo mockUser = new LoginUserInfo(1L, "testUser", Role.USER);

        try (var mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getLoginUserInfo).thenReturn(mockUser);

            when(authRedisService.existRefreshTokenByUserId(mockUser.getUserId())).thenReturn(true);
            when(authRedisService.findKeyByUserId(mockUser.getUserId())).thenReturn("dummyRefreshToken");
            when(jwtTokenProvider.getRemainingTime("dummyRefreshToken")).thenReturn(3600L);

            // when
            authCommandService.logout(accessToken);

            // then
            verify(authRedisService).deleteRefreshTokenByUserId(mockUser.getUserId());
            verify(authRedisService).addBlackListAccessToken(accessToken, 3600L);
        }
    }

    @Test
    void sendVerificationCode_success() {
        // given
        SendEmailVerifyUserRequest request = SendEmailVerifyUserRequest.builder()
                .email("test@example.com")
                .loginid("testUser")
                .build();

        // EmailVerificationCode.getCode()를 고정된 값으로 mocking
        try (MockedStatic<EmailVerificationCode> mockedCode = Mockito.mockStatic(EmailVerificationCode.class)) {
            mockedCode.when(EmailVerificationCode::getCode).thenReturn("ABC123XYZ789");

            // when
            authCommandService.sendVerificationCode(request);

            // then
            verify(authRedisService).saveValidityCode("test@example.com", "ABC123XYZ789");
            verify(mailService).sendMail("test@example.com", "testUser", MailType.VERIFICATION, "ABC123XYZ789");
        }
    }

    @Test
    void existVerificationCode_true() {
        // given
        ExistVerificationCodeRequest request = new ExistVerificationCodeRequest(
                "ABC123XYZ789",
                "test@example.com",
                "testUser"
        );

        when(authRedisService.existVerificationCode("test@example.com", "ABC123XYZ789"))
                .thenReturn(true);

        // when
        ExistVerificationCodeResponse response = authCommandService.existVerificationCode(request);

        // then
        assertNotNull(response);
        assertTrue(response.getExist());

        verify(authRedisService).existVerificationCode("test@example.com", "ABC123XYZ789");
        verify(authRedisService).deleteKeyEmail("test@example.com");
    }

    @Test
    void getUserInfo_success() {
        // given
        String email = "test@example.com";
        String loginId = "testUser";

        UserDTO userDTO = UserDTO.builder()
                .userId(1L)
                .loginId(loginId)
                .email(email)
                .status("ACTIVATE")
                .deactivatedAt(LocalDateTime.of(2025, 11, 5, 16, 0))
                .build();

        when(userMapper.findByEmailAndLoginId(email, loginId)).thenReturn(userDTO);

        // when
        FindUserInfoResponse response = authCommandService.getUserInfo(email, loginId);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(loginId, response.getLoginId());
        assertEquals(email, response.getEmail());
        assertEquals("ACTIVATE", response.getStatus());
        assertEquals(LocalDateTime.of(2025, 11, 5, 16, 0), response.getDeactivatedAt());

        // 레디스 키 삭제 호출 검증
        verify(authRedisService).deleteKeyEmail(email);
        // userMapper 호출 검증
        verify(userMapper).findByEmailAndLoginId(email, loginId);
    }

    @Test
    void resetPassword_success() {
        // given
        Long userId = 1L;
        String rawPassword = "newPassword";
        String encodedPassword = "encodedPassword";

        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .userId(userId)
                .password(rawPassword)
                .build();

        User user = new User();
        user.setPassword("oldPassword");

        when(userCommandRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // when
        authCommandService.resetPassword(request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userCommandRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(encodedPassword, savedUser.getPassword());
    }








}