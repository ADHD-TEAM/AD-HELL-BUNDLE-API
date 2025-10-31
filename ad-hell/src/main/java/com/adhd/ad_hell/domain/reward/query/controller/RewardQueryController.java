package com.adhd.ad_hell.domain.reward.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDetailResponse;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardResponse;
import com.adhd.ad_hell.domain.reward.query.service.RewardQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rewards")
public class RewardQueryController {

  private final RewardQueryService rewardQueryService;

  @GetMapping("/{rewardId}")
  public ResponseEntity<ApiResponse<RewardDetailResponse>> getRewardDetails(
      @PathVariable Long rewardId
  ) {
    RewardDetailResponse response = rewardQueryService.getRewardDetail(rewardId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<RewardResponse>>> getCategories(
      @RequestParam(required = false) String keyword
  ) {
    List<RewardResponse> list = rewardQueryService.getRewardList(keyword);
    return ResponseEntity.ok(ApiResponse.success(list));
  }
}
