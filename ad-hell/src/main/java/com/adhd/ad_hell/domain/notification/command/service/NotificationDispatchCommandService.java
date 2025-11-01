package com.adhd.ad_hell.domain.notification.command.service;

import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationScheduleRequest;
import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationSendRequest;
import com.adhd.ad_hell.domain.notification.command.dto.response.NotificationDispatchResponse;
import com.adhd.ad_hell.domain.notification.command.dto.response.NotificationScheduleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationDispatchCommandService {

    public NotificationDispatchResponse sendNotification(Long templateId, NotificationSendRequest request) {
        log.debug("Send notification template. id={}, targetType={}", templateId, request.getTargetType());
        throw new UnsupportedOperationException("Notification send is not yet implemented.");
    }

    public NotificationScheduleResponse reserveNotification(Long templateId, NotificationScheduleRequest request) {
        log.debug("Reserve notification template. id={}, schedule={}", templateId, request.getScheduledAt());
        throw new UnsupportedOperationException("Notification reservation is not yet implemented.");
    }
}