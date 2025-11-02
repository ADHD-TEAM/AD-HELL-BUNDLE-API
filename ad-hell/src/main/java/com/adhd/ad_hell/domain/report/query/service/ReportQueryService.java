package com.adhd.ad_hell.domain.report.query.service;

import com.adhd.ad_hell.domain.report.query.dto.response.ReportDetailResponse;
import com.adhd.ad_hell.domain.report.query.dto.response.ReportResponse;
import com.adhd.ad_hell.domain.report.query.mapper.ReportMapper;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDetailResponse;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDto;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardResponse;
import com.adhd.ad_hell.domain.reward.query.mapper.RewardMapper;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportQueryService {

  private final ReportMapper reportMapper;

  @Transactional(readOnly = true)
  public ReportDetailResponse getReportDetail(Long reportId) {
    ReportDetailResponse dto = reportMapper.findReportById(reportId);
    if (dto == null) {
      throw new BusinessException(ErrorCode.REWARD_NOT_FOUND);
    }
    return dto;
  }

  @Transactional(readOnly = true)
  public List<ReportResponse> getReportList() {
    return reportMapper.findReportList();
  }
}
