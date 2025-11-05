package com.adhd.ad_hell.domain.reward.command.application.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.reward.command.application.dto.request.CreateRewardRequest;
import com.adhd.ad_hell.domain.reward.command.application.dto.request.CreateRewardStockRequest;
import com.adhd.ad_hell.domain.reward.command.application.dto.request.UpdateRewardRequest;
import com.adhd.ad_hell.domain.reward.command.application.service.RewardCommandService;
import com.adhd.ad_hell.domain.reward.command.application.service.RewardStockCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rewards")
public class RewardCommandController {

  private final RewardCommandService rewardCommandService;
  private final RewardStockCommandService rewardStockCommandService;

  /* 기본 반환 전부 void 처리, 추후 변경 필요 */
  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createReward(@RequestBody CreateRewardRequest req) {
    rewardCommandService.createReward(req);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success(null));
  }

  @PutMapping("/{rewardId}")
  public ResponseEntity<ApiResponse<Void>> updateReward(@PathVariable Long rewardId, @RequestBody UpdateRewardRequest req) {
    rewardCommandService.updateReward(rewardId, req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PatchMapping("/{rewardId}/status")
  public ResponseEntity<ApiResponse<Void>> toggleStatusReward(@PathVariable Long rewardId) {
    rewardCommandService.toggleStatusReward(rewardId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping("/{rewardId}")
  public ResponseEntity<ApiResponse<Void>> deleteReward(@PathVariable Long rewardId) {
    rewardCommandService.deleteReward(rewardId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PostMapping("/{rewardId}/stocks")
  public ResponseEntity<ApiResponse<Void>> createRewardStock(
      @PathVariable Long rewardId,
      @RequestBody CreateRewardStockRequest req
  ) {
    rewardStockCommandService.createRewardStock(rewardId, req);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success(null));
  }

  @PostMapping("/{rewardId}/exchange")
  public ResponseEntity<ApiResponse<Void>> sendReward(@PathVariable Long rewardId) {
    rewardStockCommandService.sendReward(rewardId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
