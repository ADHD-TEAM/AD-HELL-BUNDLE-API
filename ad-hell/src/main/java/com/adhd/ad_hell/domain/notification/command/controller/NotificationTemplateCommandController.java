package com.adhd.ad_hell.domain.notification.command.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationTemplateCreateRequest;
import com.adhd.ad_hell.domain.notification.command.dto.request.NotificationTemplateUpdateRequest;
import com.adhd.ad_hell.domain.notification.command.dto.response.NotificationTemplateResponse;
import com.adhd.ad_hell.domain.notification.command.service.NotificationTemplateCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notifications/templates")
public class NotificationTemplateCommandController {

    private final NotificationTemplateCommandService notificationTemplateCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> createTemplate(
            @Valid @RequestBody NotificationTemplateCreateRequest request
    ) {
        NotificationTemplateResponse response = notificationTemplateCommandService.createTemplate(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> updateTemplate(
            @PathVariable Long templateId,
            @Valid @RequestBody NotificationTemplateUpdateRequest request
    ) {
        NotificationTemplateResponse response = notificationTemplateCommandService.updateTemplate(templateId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long templateId) {
        notificationTemplateCommandService.deleteTemplate(templateId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}