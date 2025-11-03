package com.adhd.ad_hell.domain.notification.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationPageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplatePageResponse;
import com.adhd.ad_hell.domain.notification.query.service.NotificationQueryService;
import com.adhd.ad_hell.domain.notification.command.infrastructure.sse.NotificationSseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class NotificationQueryController {

    private final NotificationQueryService queryService;
    private final NotificationSseEmitters emitters;

    // 유저 알림 목록(페이지)
    @GetMapping("/api/users/{userId}/notifications")
    public ResponseEntity<ApiResponse<NotificationPageResponse>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ) {
        var res = queryService.getUserNotifications(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(res));
    }

    // 미읽음 카운트
    @GetMapping("/api/users/{userId}/notifications/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable Long userId) {
        long cnt = queryService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponse.success(cnt));
    }

    // SSE 구독 (L7 idle 방지 heartbeat는 sse 패키지에서 주기 브로드캐스트)
    @GetMapping(value = "/api/users/{userId}/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable Long userId /* TODO: @AuthenticationPrincipal 검증 */) {
        return emitters.add(userId);
    }

    // 관리자 템플릿 목록/검색
    @GetMapping("/api/admin/notifications/templates")
    public ResponseEntity<ApiResponse<NotificationTemplatePageResponse>> getTemplates(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ) {
        var res = queryService.getTemplates(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(res));
    }
}
