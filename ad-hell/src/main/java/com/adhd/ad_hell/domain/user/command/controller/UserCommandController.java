package com.adhd.ad_hell.domain.user.command.controller;


import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.common.dto.CustomUserDetails;
import com.adhd.ad_hell.domain.user.command.dto.request.UserIsAvailableRequest;
import com.adhd.ad_hell.domain.user.command.dto.response.UserDetailResponse;
import com.adhd.ad_hell.domain.user.command.dto.response.UserIsAvailableResponse;
import com.adhd.ad_hell.domain.user.command.service.UserCommandService;
import com.adhd.ad_hell.domain.user.command.service.UserCommandServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserCommandController {

    private final UserCommandService userCommandService;


    /**
     * 사용할 수 있는 닉네임 확인
     * @param userIsAvailableRequest
     * @return
     */
    @GetMapping("/isAvailable")
    public ResponseEntity<ApiResponse<UserIsAvailableResponse>> isAvailable(
            @RequestParam UserIsAvailableRequest userIsAvailableRequest) {
        log.info("[UserCommandController/isAvailable] 사용할 수 있는 닉네임 확인 | {}", userIsAvailableRequest);
        UserIsAvailableResponse response = userCommandService.isAvailable(userIsAvailableRequest);
        // userIsAvailable-false : 사용해도 됨
        // userIsAvailable-true : 사용할 수 없음
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    /**
     *  마이페이지
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails
        //  @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("[UserCommandController/getUserDetail] 마이페이지 | {}", userDetails);
        // Username >> userId
        UserDetailResponse response = userCommandService.getUserDetail(userDetails);
        log.info("[UserCommandController/getUserDetail] 마이페이지 성공 ");
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
