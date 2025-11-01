package com.adhd.ad_hell.domain.notification.command.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationPushToggleRequest;
import com.adhd.ad_hell.domain.notification.command.service.NotificationPreferenceCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/settings")
public class NotificationPreferenceCommandController {

    private final NotificationPreferenceCommandService notificationPreferenceCommandService;

    @PatchMapping("/push")
    public ResponseEntity<ApiResponse<Void>> updatePushSetting(
            @Valid @RequestBody NotificationPushToggleRequest request
    ) {
        notificationPreferenceCommandService.updatePushSetting(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}