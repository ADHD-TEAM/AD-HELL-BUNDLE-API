package com.adhd.ad_hell.domain.notification.query.service;


import com.adhd.ad_hell.domain.notification.command.domain.aggregate.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.enums.YnType;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationRepository;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationTemplateRepository;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationPageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplatePageResponse;
import com.adhd.ad_hell.domain.notification.query.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NotificationQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final JpaNotificationRepository notificationRepo;
    private final JpaNotificationTemplateRepository templateRepo;
    private final NotificationMapper mapper;

    public NotificationPageResponse getUserNotifications(Long userId, int page, Integer size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), resolveSize(size),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return mapper.toNotificationPage(notificationRepo.findByUserId(userId, pageable));
    }

    public long getUnreadCount(Long userId) {
        return notificationRepo.countByUserIdAndReadYn(userId, YnType.N);
    }

    public NotificationTemplatePageResponse getTemplates(String keyword, int page, Integer size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), resolveSize(size),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<NotificationTemplate> result =
                (keyword != null && !keyword.isBlank())
                        ? templateRepo.findByDeletedYnAndTemplateTitleContainingIgnoreCase(YnType.no(), keyword, pageable)
                        : templateRepo.findByDeletedYn(YnType.no(), pageable);

        return mapper.toTemplatePage(result);
    }

    private int resolveSize(Integer size) {
        return (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size;
    }
}

