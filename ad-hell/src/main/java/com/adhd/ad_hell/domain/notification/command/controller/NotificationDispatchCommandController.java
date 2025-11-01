package com.adhd.ad_hell.domain.notification.command.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationScheduleRequest;
import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationSendRequest;
import com.adhd.ad_hell.domain.notification.command.dto.response.NotificationDispatchResponse;
import com.adhd.ad_hell.domain.notification.command.dto.response.NotificationScheduleResponse;
import com.adhd.ad_hell.domain.notification.command.service.NotificationDispatchCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notifications")
public class NotificationDispatchCommandController {

    private final NotificationDispatchCommandService notificationDispatchCommandService;

    @PostMapping("/{templateId}/send")
    public ResponseEntity<ApiResponse<NotificationDispatchResponse>> sendNotification(
            @PathVariable Long templateId,
            @Valid @RequestBody NotificationSendRequest request
    ) {
        NotificationDispatchResponse response = notificationDispatchCommandService.sendNotification(templateId, request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(response));
    }

    @PostMapping("/{templateId}/reserve")
    public ResponseEntity<ApiResponse<NotificationScheduleResponse>> reserveNotification(
            @PathVariable Long templateId,
            @Valid @RequestBody NotificationScheduleRequest request
    ) {
        NotificationScheduleResponse response = notificationDispatchCommandService.reserveNotification(templateId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}