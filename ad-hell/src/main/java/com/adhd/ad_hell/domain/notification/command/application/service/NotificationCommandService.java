package com.adhd.ad_hell.domain.notification.command.application.service;

import com.adhd.ad_hell.domain.notification.command.application.dto.request.*;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationDispatchResponse;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationScheduleResponse;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationTemplateResponse;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.enums.YnType;
import com.adhd.ad_hell.domain.notification.command.domain.event.NotificationReadEvent;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationRepository;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final JpaNotificationRepository notificationRepo;
    private final JpaNotificationTemplateRepository templateRepo;
    private final ApplicationEventPublisher publisher;

    // === 설정 변경 ===
    @Transactional
    public void updatePushSetting(NotificationPushToggleRequest request) {
        log.debug("Update push setting. memberId={}, enabled={}", request.getMemberId(), request.getPushEnabled());
        // TODO: MemberPreference 저장소/엔티티 도입 시 실제 반영
        throw new UnsupportedOperationException("Notification push setting update is not yet implemented.");
    }

    // === 읽음 처리 ===
    @Transactional
    public void markRead(Long userId, Long notificationId) {
        var n = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        if (!n.getUserId().equals(userId)) throw new IllegalStateException("본인 알림만 읽음 처리할 수 있습니다.");
        if (n.getReadYn() == YnType.N) n.markRead();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                long unread = notificationRepo.countByUserIdAndReadYn(userId, YnType.N);
                publisher.publishEvent(NotificationReadEvent.of(userId, unread));
            }
        });
    }

    // === 즉시 발송 ===
    @Transactional
    public NotificationDispatchResponse sendNotification(Long templateId, NotificationSendRequest request) {
        log.debug("Send notification. templateId={}, targetType={}", templateId, request.getTargetType());
        // TODO: 템플릿 조회, 대상자 집합 계산, 본문 머지 → Notification 벌크 저장 → 사용자별 SSE
        throw new UnsupportedOperationException("Notification send is not yet implemented.");
    }

    // === 예약 발송 ===
    @Transactional
    public NotificationScheduleResponse reserveNotification(Long templateId, NotificationScheduleRequest request) {
        log.debug("Reserve notification. templateId={}, scheduledAt={}", templateId, request.getScheduledAt());
        // TODO: NotificationSchedule 저장, 스케줄러/잡으로 배송
        throw new UnsupportedOperationException("Notification reservation is not yet implemented.");
    }

    // === 템플릿 CUD ===
    @Transactional
    public NotificationTemplateResponse createTemplate(NotificationTemplateCreateRequest request) {
        log.debug("Create template. kind={}, title={}", request.getTemplateKind(), request.getTemplateTitle());
        // TODO: 엔티티로 저장 후 Response 변환
        throw new UnsupportedOperationException("Notification template creation is not yet implemented.");
    }

    @Transactional
    public NotificationTemplateResponse updateTemplate(Long templateId, NotificationTemplateUpdateRequest request) {
        log.debug("Update template. id={}, kind={}, title={}", templateId, request.getTemplateKind(), request.getTemplateTitle());
        // TODO: 엔티티 조회/수정 후 Response 변환
        throw new UnsupportedOperationException("Notification template update is not yet implemented.");
    }

    @Transactional
    public void deleteTemplate(Long templateId) {
        log.debug("Delete template. id={}", templateId);
        // TODO: soft-delete 처리 등
        throw new UnsupportedOperationException("Notification template deletion is not yet implemented.");
    }
}
