package com.adhd.ad_hell.domain.notification.query.service;

import com.adhd.ad_hell.domain.notification.command.entity.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.command.entity.YnType;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplatePageResponse;
import com.adhd.ad_hell.domain.notification.query.repository.NotificationTemplateQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationTemplateQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final NotificationTemplateQueryRepository notificationTemplateQueryRepository;

    public NotificationTemplatePageResponse getTemplates(String keyword, int page, Integer size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), resolveSize(size), Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<NotificationTemplate> result;
        if (keyword != null && !keyword.isBlank()) {
            result = notificationTemplateQueryRepository.findByDeletedYnAndTemplateTitleContainingIgnoreCase(
                    YnType.no(),
                    keyword,
                    pageable
            );
        } else {
            result = notificationTemplateQueryRepository.findByDeletedYn(YnType.no(), pageable);
        }

        return NotificationTemplatePageResponse.from(result);
    }

    private int resolveSize(Integer size) {
        if (size == null || size <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return size;
    }
}