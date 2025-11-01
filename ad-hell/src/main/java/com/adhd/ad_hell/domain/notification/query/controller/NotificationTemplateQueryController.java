package com.adhd.ad_hell.domain.notification.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplatePageResponse;
import com.adhd.ad_hell.domain.notification.query.service.NotificationTemplateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notifications/templates")
public class NotificationTemplateQueryController {

    private final NotificationTemplateQueryService notificationTemplateQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<NotificationTemplatePageResponse>> getTemplates(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ) {
        NotificationTemplatePageResponse response = notificationTemplateQueryService.getTemplates(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}