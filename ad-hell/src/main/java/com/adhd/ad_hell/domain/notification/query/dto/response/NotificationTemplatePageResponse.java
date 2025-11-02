package com.adhd.ad_hell.domain.notification.query.dto.response;

import com.adhd.ad_hell.common.dto.Pagination;
import com.adhd.ad_hell.domain.notification.entity.NotificationTemplate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class NotificationTemplatePageResponse {

    private final List<NotificationTemplateSummaryResponse> templates;
    private final Pagination pagination;

    public static NotificationTemplatePageResponse from(Page<NotificationTemplate> page) {
        List<NotificationTemplateSummaryResponse> items = page.getContent().stream()
                .map(NotificationTemplateSummaryResponse::from)
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
}