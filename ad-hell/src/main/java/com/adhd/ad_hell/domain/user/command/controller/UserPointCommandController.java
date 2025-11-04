package com.adhd.ad_hell.domain.user.command.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.user.command.dto.request.UserPointRequest;
import com.adhd.ad_hell.domain.user.command.dto.response.UserPointResponse;
import com.adhd.ad_hell.domain.user.command.service.PointCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserPointCommandController {

  private final PointCommandService pointCommandService;

  @PostMapping("/point")
  public ResponseEntity<ApiResponse<UserPointResponse>> earnPoint(@RequestBody UserPointRequest userPointRequest) {
    UserPointResponse response = pointCommandService.earnPoints(userPointRequest);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
