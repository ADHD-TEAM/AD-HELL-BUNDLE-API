package com.adhd.ad_hell.domain.notification.query.dto.response;

import com.adhd.ad_hell.domain.notification.command.domain.aggregate.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.enums.NotificationTemplateKind;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationTemplateSummaryResponse {

    private final Long templateId;
    private final NotificationTemplateKind templateKind;
    private final String templateTitle;
    private final String templateBody;
    private final LocalDateTime createdAt;

    public static NotificationTemplateSummaryResponse from(NotificationTemplate template) {
        return NotificationTemplateSummaryResponse.builder()
                .templateId(template.getId())
                .templateKind(template.getTemplateKind())
                .templateTitle(template.getTemplateTitle())
                .templateBody(template.getTemplateBody())
                .createdAt(template.getCreatedAt())
                .build();
    }
}