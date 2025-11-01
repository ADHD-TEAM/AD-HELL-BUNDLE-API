package com.adhd.ad_hell.domain.notification.command.dto.response;

import com.adhd.ad_hell.domain.notification.command.entity.NotificationTemplateKind;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationTemplateResponse {
    private final Long templateId;
    private final NotificationTemplateKind templateKind;
    private final String templateTitle;
    private final String templateBody;
}