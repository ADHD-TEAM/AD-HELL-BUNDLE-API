package com.adhd.ad_hell.domain.user.command.dto.response;

import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.command.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDetailResponse {

    private String loginId;
    private String nickname;
    private String email;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime deactivatedAt;
    private Integer amount;

}
