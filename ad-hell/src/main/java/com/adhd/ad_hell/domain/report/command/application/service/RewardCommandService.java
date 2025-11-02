package com.adhd.ad_hell.domain.report.command.application.service;

import com.adhd.ad_hell.domain.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.domain.category.query.service.provider.CategoryProvider;
import com.adhd.ad_hell.domain.report.command.application.dto.request.CreateReportRequest;
import com.adhd.ad_hell.domain.report.command.domain.aggregate.Report;
import com.adhd.ad_hell.domain.report.command.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewardCommandService {

  private final ReportRepository reportRepository;
  private final CategoryProvider categoryProvider;


  @Transactional
  public void createReport(CreateReportRequest req) {
    Category category = categoryProvider.getCategoryEntityById(req.getCategoryId());

    Report report = Report.builder()
        .category(category)
        .targetId(req.getTargetId())
        .reasonDetail(req.getReasonDetail())
                          .build();

    reportRepository.save(report);
  }
}
