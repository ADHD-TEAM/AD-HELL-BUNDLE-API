package com.adhd.ad_hell.domain.report.command.application.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.report.command.application.dto.request.CreateReportRequest;
import com.adhd.ad_hell.domain.report.command.application.service.RewardCommandService;
import com.adhd.ad_hell.domain.reward.command.application.dto.request.CreateRewardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportCommandController {

  private final RewardCommandService rewardCommandService;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createReport(
      @RequestBody CreateReportRequest req
  ) {
    rewardCommandService.createReport(req);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success(null));
  }
}
