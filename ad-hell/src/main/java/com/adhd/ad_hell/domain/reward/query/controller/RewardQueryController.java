package com.adhd.ad_hell.domain.reward.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.reward.query.dto.request.RewardSearchRequest;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDetailResponse;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardListResponse;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardStockResponse;
import com.adhd.ad_hell.domain.reward.query.service.RewardQueryService;
import com.adhd.ad_hell.domain.reward.query.service.RewardStockQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rewards")
public class RewardQueryController {

  private final RewardQueryService rewardQueryService;
  private final RewardStockQueryService rewardStockQueryService;

  @GetMapping("/{rewardId}")
  public ResponseEntity<ApiResponse<RewardDetailResponse>> getRewardDetails(
      @PathVariable Long rewardId
  ) {
    RewardDetailResponse response = rewardQueryService.getRewardDetail(rewardId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<RewardListResponse>> getRewards(
      RewardSearchRequest request
  ) {
    RewardListResponse list = rewardQueryService.getRewardList(request);
    return ResponseEntity.ok(ApiResponse.success(list));
  }

  @GetMapping("/{rewardId}/stock")
  public ResponseEntity<ApiResponse<List<RewardStockResponse>>> getRewardStocks(
      @PathVariable Long rewardId
  ) {
    List<RewardStockResponse> list = rewardStockQueryService.getRewardStockList(rewardId);
    return ResponseEntity.ok(ApiResponse.success(list));
  }
}
