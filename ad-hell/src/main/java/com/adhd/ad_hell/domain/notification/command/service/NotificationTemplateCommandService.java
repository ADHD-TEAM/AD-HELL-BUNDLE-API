package com.adhd.ad_hell.domain.notification.command.service;

import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationTemplateCreateRequest;
import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationTemplateUpdateRequest;
import com.adhd.ad_hell.domain.notification.command.dto.response.NotificationTemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationTemplateCommandService {

    public NotificationTemplateResponse createTemplate(NotificationTemplateCreateRequest request) {
        log.debug(
                "Create notification template: kind={}, title={}",
                request.getTemplateKind(),
                request.getTemplateTitle()
        );
        throw new UnsupportedOperationException("Notification template creation is not yet implemented.");
    }

    public NotificationTemplateResponse updateTemplate(Long templateId, NotificationTemplateUpdateRequest request) {
        log.debug(
                "Update notification template. id={}, kind={}, title={}",
                templateId,
                request.getTemplateKind(),
                request.getTemplateTitle()
        );
        throw new UnsupportedOperationException("Notification template update is not yet implemented.");
    }

    public void deleteTemplate(Long templateId) {
        log.debug("Delete notification template. id={}", templateId);
        throw new UnsupportedOperationException("Notification template deletion is not yet implemented.");
    }
}