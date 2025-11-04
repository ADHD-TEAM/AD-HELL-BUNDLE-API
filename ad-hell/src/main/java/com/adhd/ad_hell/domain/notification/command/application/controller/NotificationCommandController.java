package com.adhd.ad_hell.domain.notification.command.application.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.notification.command.application.dto.request.*;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationDispatchResponse;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationScheduleResponse;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationTemplateResponse;
import com.adhd.ad_hell.domain.notification.command.application.service.NotificationCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class NotificationCommandController {

    private final NotificationCommandService commandService;

    // --- 사용자 설정/읽음 ---
    @PatchMapping("/api/notifications/settings/push")
    public ResponseEntity<ApiResponse<Void>> updatePushSetting(@Valid @RequestBody NotificationPushToggleRequest request) {
        commandService.updatePushSetting(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/api/users/{userId}/notifications/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markRead(@PathVariable Long userId, @PathVariable Long notificationId) {
        commandService.markRead(userId, notificationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // --- 관리자: 즉시/예약 발송 ---
    @PostMapping("/api/admin/notifications/{templateId}/send")
    public ResponseEntity<ApiResponse<NotificationDispatchResponse>> send(
            @PathVariable Long templateId, @Valid @RequestBody NotificationSendRequest request) {

        var res = commandService.sendNotification(templateId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success(res));
    }

    @PostMapping("/api/admin/notifications/{templateId}/reserve")
    public ResponseEntity<ApiResponse<NotificationScheduleResponse>> reserve(
            @PathVariable Long templateId, @Valid @RequestBody NotificationScheduleRequest request) {

        var res = commandService.reserveNotification(templateId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    // --- 관리자: 템플릿 CUD ---
    @PostMapping("/api/admin/notifications/templates")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> createTemplate(
            @Valid @RequestBody NotificationTemplateCreateRequest request) {
        var res = commandService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    @PutMapping("/api/admin/notifications/templates/{templateId}")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> updateTemplate(
            @PathVariable Long templateId, @Valid @RequestBody NotificationTemplateUpdateRequest request) {
        var res = commandService.updateTemplate(templateId, request);
        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @DeleteMapping("/api/admin/notifications/templates/{templateId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long templateId) {
        commandService.deleteTemplate(templateId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
