package com.adhd.ad_hell.domain.auth.command.controller;

import com.adhd.ad_hell.common.ApiEndpoint;
import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.auth.command.dto.request.LoginRequest;
import com.adhd.ad_hell.domain.auth.command.dto.response.TokenResponse;
import com.adhd.ad_hell.domain.auth.command.service.AuthCommandService;
import com.adhd.ad_hell.domain.user.command.dto.request.UserSignUpRequest;
import com.adhd.ad_hell.domain.user.command.entity.Role;
import com.adhd.ad_hell.domain.user.command.service.UserCommandService;
import com.adhd.ad_hell.domain.user.command.service.UserCommandServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.token.TokenService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthCommandController {

    private final UserCommandService userCommandService;
    private final AuthCommandService authCommandService;

    /**
     * 회원가입
     * @param userSignUpRequest
     * @return
     */
    @PostMapping("/signUp/{type}")
    public ResponseEntity<ApiResponse<Void>> signUp(
            @Validated @RequestBody UserSignUpRequest userSignUpRequest
            , @PathVariable Role type
    ) {
        log.info("[AuthCommandController/signUp] 회원가입 | {}", userSignUpRequest);
        userCommandService.singUp(userSignUpRequest,type);

        log.info("[AuthCommandController/signUp] 회원가입 성공 | {}", userSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
           @Valid @RequestBody LoginRequest request
    ) {
        log.info("[AuthCommandController/login] 로그인 | {}", request);
        TokenResponse response = authCommandService.login(request);

        log.info("[AuthCommandController/login] 로그인 성공 | {}", request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 로그아웃
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        log.info("[AuthCommandController/login] logout |");
        // authCommandService.logout()

        return ResponseEntity.ok(ApiResponse.success(null));
    }





}
