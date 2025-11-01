package com.adhd.ad_hell.domain.notification.command.service;

import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationPushToggleRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationPreferenceCommandService {

    public void updatePushSetting(NotificationPushToggleRequest request) {
        log.debug("Update push setting. memberId={}, enabled={}", request.getMemberId(), request.getPushEnabled());
        throw new UnsupportedOperationException("Notification push setting update is not yet implemented.");
    }
}