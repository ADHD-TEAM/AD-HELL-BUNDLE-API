package com.adhd.ad_hell.domain.user.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.user.command.dto.request.UserPointRequest;
import com.adhd.ad_hell.domain.user.command.dto.response.UserPointResponse;
import com.adhd.ad_hell.domain.user.query.dto.response.UserPointHistoryResponse;
import com.adhd.ad_hell.domain.user.query.service.UserPointQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserPointQueryController {

  private final UserPointQueryService userPointQueryService;

  @GetMapping("/point")
  public ResponseEntity<ApiResponse<List<UserPointHistoryResponse>>> getMyPointHistory() {
    List<UserPointHistoryResponse> response = userPointQueryService.getMyPointHistory();
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
