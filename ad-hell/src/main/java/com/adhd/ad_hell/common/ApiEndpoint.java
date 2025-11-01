package com.adhd.ad_hell.common;

import com.adhd.ad_hell.domain.user.command.entity.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiEndpoint {

    // Auth 관련
    AUTH(HttpMethod.POST,"/api/auth/**" , null),

    // User 관련
    USER_IS_AVAILABLE(HttpMethod.GET,"/api/users/isAvailable", null),
    USER_ME(HttpMethod.GET,"/api/users/me", Role.USER),



    ;
    private final HttpMethod endpointStatus;
    private final String path;
    private final Role role;


}
