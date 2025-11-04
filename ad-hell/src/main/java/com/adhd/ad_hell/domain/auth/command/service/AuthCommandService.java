package com.adhd.ad_hell.domain.auth.command.service;


import com.adhd.ad_hell.common.dto.CustomUserDetails;
import com.adhd.ad_hell.domain.auth.command.dto.request.LoginRequest;
import com.adhd.ad_hell.domain.auth.command.dto.request.SendEmailVerifyUserRequest;
import com.adhd.ad_hell.domain.auth.command.dto.response.TokenResponse;

public interface AuthCommandService {
    TokenResponse login(LoginRequest request);
    void logout(CustomUserDetails customUserDetails);
    void sendEmail(SendEmailVerifyUserRequest request);
}
