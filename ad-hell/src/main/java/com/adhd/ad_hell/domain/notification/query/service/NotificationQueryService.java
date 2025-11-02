package com.adhd.ad_hell.domain.notification.query.service;

import com.adhd.ad_hell.domain.notification.entity.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.entity.enums.YnType;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationPageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplatePageResponse;
import com.adhd.ad_hell.domain.notification.repository.NotificationRepository;
import com.adhd.ad_hell.domain.notification.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final NotificationRepository notificationRepo;
    private final NotificationTemplateRepository templateRepo;

    public NotificationPageResponse getUserNotifications(Long userId, int page, Integer size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), resolveSize(size), Sort.by(Sort.Direction.DESC, "createdAt"));
        return NotificationPageResponse.from(notificationRepo.findByUserId(userId, pageable));
    }

    public long getUnreadCount(Long userId) {
        return notificationRepo.countByUserIdAndReadYn(userId, YnType.N);
    }

    public NotificationTemplatePageResponse getTemplates(String keyword, int page, Integer size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), resolveSize(size), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<NotificationTemplate> result;
        if (keyword != null && !keyword.isBlank()) {
            result = templateRepo.findByDeletedYnAndTemplateTitleContainingIgnoreCase(YnType.no(), keyword, pageable);
        } else {
            result = templateRepo.findByDeletedYn(YnType.no(), pageable);
        }
        return NotificationTemplatePageResponse.from(result);
    }

    private int resolveSize(Integer size) {
        return (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size;
    }
}
