package com.adhd.ad_hell.domain.notification.query.mapper;

import com.adhd.ad_hell.common.dto.Pagination;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationScheduleResponse;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationTemplateResponse;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.Notification;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.NotificationSchedule;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationPageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationSummaryResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplatePageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplateSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    /* ---------- Notification(알림) ---------- */

    public NotificationSummaryResponse toNotificationSummary(Notification n) {
        return NotificationSummaryResponse.builder()
                .notificationId(n.getId())
                .notificationTitle(n.getNotificationTitle())
                .notificationBody(n.getNotificationBody())
                .read(n.getReadYn().isYes())
                .createdAt(n.getCreatedAt())
                .build();
    }

    public NotificationPageResponse toNotificationPage(Page<Notification> page) {
        List<NotificationSummaryResponse> items = page.getContent()
                .stream()
                .map(this::toNotificationSummary)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .build();

        return NotificationPageResponse.builder()
                .notifications(items)
                .pagination(pagination)
                .build();
    }

    /* ---------- Template(템플릿) ---------- */

    public NotificationTemplateSummaryResponse toTemplateSummary(NotificationTemplate t) {
        return NotificationTemplateSummaryResponse.builder()
                .templateId(t.getId())
                .templateKind(t.getTemplateKind())
                .templateTitle(t.getTemplateTitle())
                .templateBody(t.getTemplateBody())
                .createdAt(t.getCreatedAt())
                .build();
    }

    public NotificationTemplatePageResponse toTemplatePage(Page<NotificationTemplate> page) {
        List<NotificationTemplateSummaryResponse> items = page.getContent()
                .stream()
                .map(this::toTemplateSummary)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .build();

        return NotificationTemplatePageResponse.builder()
                .templates(items)
                .pagination(pagination)
                .build();
    }

    public NotificationTemplateResponse toTemplateResponse(NotificationTemplate t) {
        return NotificationTemplateResponse.builder()
                .templateId(t.getId())
                .templateKind(t.getTemplateKind())
                .templateTitle(t.getTemplateTitle())
                .templateBody(t.getTemplateBody())
                .build();
    }

    /* ---------- Schedule(예약) ---------- */

    public NotificationScheduleResponse toScheduleResponse(NotificationSchedule s) {
        return NotificationScheduleResponse.builder()
                .scheduleId(s.getId())
                .scheduleStatus(s.getScheduleStatus())
                .scheduledAt(s.getScheduledAt())
                .sentAt(s.getSentAt())
                .build();
    }
}
