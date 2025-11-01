package com.adhd.ad_hell.domain.notification.query.dto.response;

import com.adhd.ad_hell.domain.notification.command.entity.Notification;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationSummaryResponse {

    private final Long notificationId;
    private final String notificationTitle;
    private final String notificationBody;
    private final boolean read;
    private final LocalDateTime createdAt;

    public static NotificationSummaryResponse from(Notification notification) {
        return NotificationSummaryResponse.builder()
                .notificationId(notification.getId())
                .notificationTitle(notification.getNotificationTitle())
                .notificationBody(notification.getNotificationBody())
                .read(notification.getReadYn().isYes())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}