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

    // USER 관련
    USER_IS_AVAILABLE(HttpMethod.GET,"/api/users/isAvailable", null),
    USER_ME(HttpMethod.GET,"/api/users/**", Role.USER),
    USER_MODIFY(HttpMethod.PUT,"/api/users/**", Role.USER),
    USER_PATCH(HttpMethod.PATCH,"/api/users/**", Role.USER),

    // ADMIN 관련
    ADMIN_USER_LIST(HttpMethod.POST,"/api/admins/**", Role.ADMIN),
    ADMIN_USER_DETAIL(HttpMethod.GET,"/api/admins/**", Role.ADMIN),
    ADMIN_USER_MODIFY(HttpMethod.PUT,"/api/admins/**", Role.ADMIN),
    ADMIN_USER_PATCH(HttpMethod.PATCH,"/api/admins/**", Role.ADMIN),





    ;
    private final HttpMethod endpointStatus;
    private final String path;
    private final Role role;


}
