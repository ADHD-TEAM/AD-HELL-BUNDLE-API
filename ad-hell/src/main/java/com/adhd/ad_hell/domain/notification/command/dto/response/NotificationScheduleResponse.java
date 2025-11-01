package com.adhd.ad_hell.domain.notification.command.dto.response;

import com.adhd.ad_hell.domain.notification.command.entity.NotificationScheduleStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationScheduleResponse {
    private final Long scheduleId;
    private final NotificationScheduleStatus scheduleStatus;
    private final LocalDateTime scheduledAt;
    private final LocalDateTime sentAt;
}