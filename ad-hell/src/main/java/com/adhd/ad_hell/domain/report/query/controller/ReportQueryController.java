package com.adhd.ad_hell.domain.report.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.report.query.dto.request.ReportSearchRequest;
import com.adhd.ad_hell.domain.report.query.dto.response.ReportDetailResponse;
import com.adhd.ad_hell.domain.report.query.dto.response.ReportListResponse;
import com.adhd.ad_hell.domain.report.query.dto.response.ReportResponse;
import com.adhd.ad_hell.domain.report.query.service.ReportQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportQueryController {

  private final ReportQueryService reportQueryService;

  @GetMapping("/{reportId}")
  public ResponseEntity<ApiResponse<ReportDetailResponse>> getReportDetails(
      @PathVariable Long reportId
  ) {
    ReportDetailResponse response = reportQueryService.getReportDetail(reportId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<ReportListResponse>> getReports(
      ReportSearchRequest request
  ) {
    ReportListResponse list = reportQueryService.getReportList(request);
    return ResponseEntity.ok(ApiResponse.success(list));
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<ReportListResponse>> getMyReports(
      ReportSearchRequest request
  ) {
    ReportListResponse list = reportQueryService.getMyReports(request);
    return ResponseEntity.ok(ApiResponse.success(list));
  }

  @GetMapping("/me/{reportId}")
  public ResponseEntity<ApiResponse<ReportDetailResponse>> getMyReportDetail(
      @PathVariable Long reportId
  ) {
    ReportDetailResponse detail = reportQueryService.getMyReportDetail(reportId);
    return ResponseEntity.ok(ApiResponse.success(detail));
  }
}
