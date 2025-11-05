package com.adhd.ad_hell.domain.auth.command.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindUserInfoResponse {
    private Long userId;
    private String loginId;
    private String email;
    private String status;
    private LocalDateTime deactivatedAt;

}
