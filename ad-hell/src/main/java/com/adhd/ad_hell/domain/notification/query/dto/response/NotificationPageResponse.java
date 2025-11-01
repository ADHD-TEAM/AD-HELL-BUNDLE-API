package com.adhd.ad_hell.domain.notification.query.dto.response;

import com.adhd.ad_hell.common.dto.Pagination;
import com.adhd.ad_hell.domain.notification.command.entity.Notification;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class NotificationPageResponse {

    private final List<NotificationSummaryResponse> notifications;
    private final Pagination pagination;

    public static NotificationPageResponse from(Page<Notification> page) {
        List<NotificationSummaryResponse> items = page.getContent().stream()
                .map(NotificationSummaryResponse::from)
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
}