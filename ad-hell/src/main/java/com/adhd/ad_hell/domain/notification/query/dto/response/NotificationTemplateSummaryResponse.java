package com.adhd.ad_hell.domain.notification.query.dto.response;

import com.adhd.ad_hell.domain.notification.entity.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.entity.enums.NotificationTemplateKind;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

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