package com.adhd.ad_hell.domain.user.command.entity;


import com.adhd.ad_hell.common.BaseTimeEntity;
import com.adhd.ad_hell.domain.user.command.dto.request.UserSignUpRequest;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name="user_info")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role roleType;

    @Column(nullable = false, length = 30)
    private String loginId;

   // @Column(nullable = false, columnDefinition = "TEXT")
   @Column(nullable = false, length = 600)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVATE;

    private LocalDateTime deactivatedAt;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private Long amount = 0L;

}
